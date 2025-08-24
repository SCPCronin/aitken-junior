package com.scronin.aitken_junior.Common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

// src/main/java/com/example/db/JdbcConfig.java
@Configuration
public class JdbcConfig {

    // Optional: Boot auto-creates this if DataSource exists.
    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource ds) {
        return new NamedParameterJdbcTemplate(ds);
    }

    // Spring 6+: a fluent, minimal abstraction that still keeps SQL strings
    @Bean
    public org.springframework.jdbc.core.simple.JdbcClient jdbcClient(DataSource ds) {
        return org.springframework.jdbc.core.simple.JdbcClient.create(ds);
    }
}
