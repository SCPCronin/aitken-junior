package com.scronin.aitken_junior;

import com.scronin.aitken_junior.Common.Exceptions.AitkenJuniorException;
import com.scronin.aitken_junior.Common.db.PostgresClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class AitkenJuniorApplication {
	public static void main(String[] args) {
		SpringApplication.run(AitkenJuniorApplication.class, args);
	}
}

@RestController
@RequestMapping("/api")
class HelloController {

	@Autowired
	PostgresClient postgresClient;

	public record TestRecord(Long id, String name, Instant createdAt) {};

	@GetMapping("/hello")
	public String hello() {

		RowMapper<TestRecord> rowMapper = (rs, rowNum) -> new TestRecord(
			rs.getLong("id"),
			rs.getString("name"),
			rs.getTimestamp("created_at").toInstant()
		);

		try {
			postgresClient.createSchemaIfNotExists("hobby_tracking");
			postgresClient.createTableIfNotExists("hobby_tracking", "games", "(\n" +
					"    id UUID PRIMARY KEY,  \n" +
					"    name VARCHAR(255) NOT NULL,                    \n" +
					"    time TIMESTAMPTZ NOT NULL DEFAULT NOW(),      \n" +
					"    thoughts TEXT                                   \n" +
					")");
			postgresClient.dropTableIfExists("hobby_tracking", "games");
		} catch (AitkenJuniorException e) {
			return e.toString();
		}

		Map<String, Object> params = new HashMap<>();

		Random random = new Random();
		params.put("id", random.nextInt());
		params.put("name", "Nicole");
		params.put("created_at", Timestamp.from(Instant.now()));

		postgresClient.insert("test_schema", "test_table", params);

		List<TestRecord> users = (List<TestRecord>) postgresClient.select("test_schema", "test_table", null, null, rowMapper);

		for (TestRecord user : users) {
			System.out.println("User: " + user.name);
		}

		postgresClient.delete("test_schema", "test_table", "name = 'Nicole'");

		List<TestRecord> usersAfterDeletion = (List<TestRecord>) postgresClient.select("test_schema", "test_table", null, null, rowMapper);

		for (TestRecord user : users) {
			System.out.println("User: " + user.name);
		}

		return "Connected to the database!";

	}
}
