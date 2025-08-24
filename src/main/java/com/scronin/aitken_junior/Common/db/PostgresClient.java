package com.scronin.aitken_junior.Common.db;

import com.scronin.aitken_junior.Common.Exceptions.AitkenJuniorException;
import com.scronin.aitken_junior.interfaces.IDatabaseClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Component
public class PostgresClient implements IDatabaseClient {
    private final NamedParameterJdbcTemplate jdbc;

    public PostgresClient(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /** Validate identifiers to prevent injection via schema/table/column names. */
    private static String id(String identifier) {

        // Pattern to say that an identifier must start with a letter or underscore,
        // and can be followed by letters, digits, or underscores.
        Pattern validIdentifierRegEx = Pattern.compile("^[A-Za-z_][A-Za-z0-9_]*$");

        if (!validIdentifierRegEx.matcher(identifier).matches()) {
            log.error("Potential SQL injection attempt with identifier: {}", identifier);
            throw new IllegalArgumentException("Invalid identifier: " + identifier);
        }
        return identifier; // safe to interpolate as identifier (not a value)
    }

    /** Example: select with fully-qualified schema.table, parameterized values */
    public <T> List<T> select(
            String schema, String table, String whereSql,
            Map<String, ?> params, RowMapper<T> rowMapper) {

        String sql = String.format(
                "SELECT * FROM %s.%s %s",
                id(schema), id(table),
                (whereSql == null || whereSql.isBlank()) ? "" : "WHERE " + whereSql);

        return jdbc.query(sql, params, rowMapper);
    }


    /**
     * Insert data into a table. Params is a key-value map of column names to values.
     * @param schema - The schema name
     * @param table - The table name
     * @param params - Params is a key-value map of column names to values.
     */
    public void insert(String schema, String table, Map<String, ?> params) {
        String columns = String.join(", ", params.keySet().stream().map(PostgresClient::id).toList());
        String values = String.join(", ", params.keySet().stream().map(k -> ":" + k).toList());

        String sql = String.format(
                "INSERT INTO %s.%s (%s) VALUES (%s)",
                id(schema), id(table), columns, values);

        jdbc.update(sql, params);
    }

    public void update(String schema, String table, Map<String, ?> updateParams, String whereSql) {
        try {
            String setClause = String.join(", ", updateParams.keySet().stream().map(k -> id(k) + " = :" + k).toList());
            String sql = String.format(
                    "UPDATE %s.%s SET %s %s",
                    id(schema), id(table), setClause,
                    (whereSql == null || whereSql.isBlank()) ? "" : "WHERE " + whereSql);
            jdbc.update(sql, updateParams);
        } catch (Exception e) {
            log.error("Error updating table: {}", e.getMessage());
            throw new RuntimeException("Error while updating table", e);
        }
    }

    public void delete(String schema, String table, String whereSql) {
        try {
            String sql = String.format(
                    "DELETE FROM %s.%s %s",
                    id(schema), id(table),
                    (whereSql == null || whereSql.isBlank()) ? "" : "WHERE " + whereSql);
            jdbc.getJdbcTemplate().execute(sql);
        } catch (Exception e) {
            log.error("Error deleting from table: {}", e.getMessage());
            throw new RuntimeException("Error while deleting from table", e);
        }
    }

    public void createSchemaIfNotExists(String schemaName) throws AitkenJuniorException {
        try {
            String sql = String.format("CREATE SCHEMA IF NOT EXISTS %s", schemaName);
            jdbc.getJdbcTemplate().execute(sql);
        } catch (BadSqlGrammarException e) {
            log.error("Schema Name Provided is invalid for creating schema: {}", schemaName);
            throw new AitkenJuniorException("Schema Name Provided is invalid", 1001, e);
        } catch (Exception e) {
            log.error("Error creating schema: {}", e.getMessage());
            throw new AitkenJuniorException("Error creating schema", 1000, e);
        }
    }

    public void dropSchemaIfExists(String schemaName) throws AitkenJuniorException {
        try {
            String sql = String.format("DROP SCHEMA IF EXISTS %s CASCADE", schemaName);
            jdbc.getJdbcTemplate().execute(sql);
        } catch (BadSqlGrammarException e) {
            log.error("Schema Name Provided is invalid for dropping schema: {}", schemaName);
            throw new AitkenJuniorException("Schema Name Provided is invalid", 1002, e);
        } catch (Exception e) {
            log.error("Error dropping schema: {}", e.getMessage());
            throw new AitkenJuniorException("Error dropping schema", 1000, e);
        }
    }

    public boolean schemaExists(String schemaName) throws AitkenJuniorException {
        try {
            String sql = "SELECT schema_name FROM information_schema.schemata WHERE schema_name = :schemaName";
            Map<String, Object> params = Map.of("schemaName", schemaName);
            List<String> schemas = jdbc.queryForList(sql, params, String.class);
            if (schemas.isEmpty()) {
                log.debug("Schema does not exist: {}", schemaName);
                return false;
            }
            log.debug("Schema exists: {}", schemaName);
            return true;
        } catch (BadSqlGrammarException e) {
            log.error("Schema Name Provided is invalid for validating schema: {}", schemaName);
            throw new AitkenJuniorException("Schema Name Provided is invalid", 1003, e);
        } catch (Exception e) {
            log.error("Error validating schema: {}", e.getMessage());
            throw new AitkenJuniorException("Error while executing SQL", 1000, e);
        }
    }

    public void createTableIfNotExists(String schemaName, String tableName, String tableDefinition) throws AitkenJuniorException {
        try {
            String sql = String.format(
                    "CREATE TABLE IF NOT EXISTS %s.%s %s",
                    id(schemaName), id(tableName), tableDefinition);
            jdbc.getJdbcTemplate().execute(sql);
        } catch (Exception e) {
            log.error("Error creating table: {}", e.getMessage());
            throw new AitkenJuniorException("Error while creating table", 1004, e);
        }
    }

    public boolean tableExists(String schemaName, String tableName) throws AitkenJuniorException {
        try {
            String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = :schemaName AND table_name = :tableName";
            Map<String, Object> params = Map.of("schemaName", schemaName, "tableName", tableName);
            List<String> tables = jdbc.queryForList(sql, params, String.class);
            if (tables.isEmpty()) {
                log.debug("Table not found, returning false");
                return false;
            }
            log.debug("Table exists: {}.{}", schemaName, tableName);
            return true;
        } catch (BadSqlGrammarException e) {
            log.error("Schema or Table Name Provided is invalid for validating table: {}.{}", schemaName, tableName);
            throw new AitkenJuniorException("Schema or Table Name Provided is invalid", 1003, e);
        } catch (Exception e) {
            log.error("Error validating table: {}", e.getMessage());
            throw new AitkenJuniorException("Error while executing SQL", 1000, e);
        }
    }

    public void dropTableIfExists(String schemaName, String tableName) throws AitkenJuniorException {
        try {
            String sql = String.format("DROP TABLE IF EXISTS %s.%s", id(schemaName), id(tableName));
            jdbc.getJdbcTemplate().execute(sql);
        } catch (Exception e) {
            log.error("Error dropping table: {}", e.getMessage());
            throw new AitkenJuniorException("Error while dropping table", 1005, e);
        }
    }

    public int execute(String sql, Map<String, ?> params) {
        return jdbc.update(sql, params); // uses PreparedStatement under the hood
    }
}
