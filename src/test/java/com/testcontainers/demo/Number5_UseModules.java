package com.testcontainers.demo;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.sql.*;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Number5_UseModules {

    // Using GenericContainer to create a PostgreSQL container

    PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withCopyFileToContainer(MountableFile.forClasspathResource("schema.sql"),
                    "/docker-entrypoint-initdb.d/01-schema.sql");

    @Test
    void shouldGetPendingTodos() throws SQLException {
        postgres.start();
        var connection = DriverManager.getConnection(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        var query = "SELECT id, title, completed, order_number FROM todos WHERE completed = TRUE";
        List<Map<String, Object>> completedTodos = new ArrayList<>();

        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            completedTodos.add(Map.of(
                    "id", rs.getString("id"),
                    "title", rs.getString("title"),
                    "completed", rs.getBoolean("completed"),
                    "order_number", rs.getInt("order_number")
            ));
        }
        assertEquals(2, completedTodos.size(), "Expected 2 completed todos");
        System.out.println(completedTodos);
    }
}