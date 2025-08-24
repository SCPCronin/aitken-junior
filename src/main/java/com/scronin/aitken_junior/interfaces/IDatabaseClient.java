package com.scronin.aitken_junior.interfaces;

import com.scronin.aitken_junior.Common.Exceptions.AitkenJuniorException;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;

public interface IDatabaseClient {

    /**
     * Create a table if it does not already exist.
     *
     * @param schema - The schema name
     * @throws AitkenJuniorException - The exception that's thrown if the schema cannot be created
     */
    void createSchemaIfNotExists(String schema) throws AitkenJuniorException;

    /**
     * Drops the schema if it doesn't exist
     *
     * @param schema - The name of the schema top drop
     * @throws AitkenJuniorException - An exception that's thrown if the schema cannot be dropped
     */
    void dropSchemaIfExists(String schema) throws AitkenJuniorException;

    /**
     * Create a table if it does not already exist.
     *
     * @param schema - The name of the schema to check
     * @return - True if the schema exists, false otherwise
     * @throws AitkenJuniorException - An exception that's thrown in the event that we cannot check for the schema
     */
    boolean schemaExists(String schema) throws AitkenJuniorException;

    /**
     * Create a table if it does not already exist.
     *
     * @param schema          - The schema name
     * @param tableName       - The table name
     * @param tableDefinition - The table definition (e.g. "(id SERIAL PRIMARY KEY, name VARCHAR(255))")
     * @throws AitkenJuniorException - An exception that's thrown if the table cannot be created
     */
    void createTableIfNotExists(String schema, String tableName, String tableDefinition) throws AitkenJuniorException;

    /**
     * Drops a table if it exists
     *
     * @param schema    - The schema name
     * @param tableName - The table name
     * @throws AitkenJuniorException - An exception that's thrown if the table cannot be dropped
     */
    void dropTableIfExists(String schema, String tableName) throws AitkenJuniorException;

    /**
     * Checks if a table exists in a given schema
     *
     * @param schema    - The schema name
     * @param tableName - The table name
     * @return - True if the table exists, false otherwise
     * @throws AitkenJuniorException - An exception that's thrown if we cannot check for the table
     */
    boolean tableExists(String schema, String tableName) throws AitkenJuniorException;

    /**
     * Executes a select statement and returns the results as a list of objects
     *
     * @param sql       - The SQL statement to execute
     * @param params    - The parameters to bind to the SQL statement
     * @param rowMapper - The row mapper to use to map the results to objects
     * @param <T>       - The type of object to return
     * @return - A list of objects
     * @throws AitkenJuniorException - An exception that's thrown if the select statement cannot be executed
     */
    <T> List<T> select(String schema, String table, String sql, Map<String, ?> params, RowMapper<T> rowMapper) throws AitkenJuniorException;

    /**
     * Inserts a record into a table
     *
     * @param schema    - The schema name
     * @param tableName - The table name
     * @param params    - The parameters to bind to the insert statement
     * @throws AitkenJuniorException - An exception that's thrown if the insert statement cannot be executed
     */
    void insert(String schema, String tableName, Map<String, ?> params) throws AitkenJuniorException;

    /**
     * Deletes records from a table based on the provided parameters
     *
     * @param schema    - The schema name
     * @param tableName - The table name
     * @param whereSql    - The parameters to bind to the delete statement (used in the WHERE clause)
     * @throws AitkenJuniorException - An exception that's thrown if the delete statement cannot be executed
     */
    void delete(String schema, String tableName, String whereSql) throws AitkenJuniorException;

}
