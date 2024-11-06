package com.atomicjar.todos.smoke;

import com.atomicjar.todos.entity.Todo;
import com.atomicjar.todos.repository.TodoRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class Number4_DontMountFiles {

    @Autowired
    TodoRepository repository;

    @LocalServerPort
    private Integer port;

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withFileSystemBind(
                            "src/test/resources/schema.sql",
                            "/docker-entrypoint-initdb.d/01-schema.sql",
                            BindMode.READ_ONLY);

    @DynamicPropertySource
    public static void setUp(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void shouldSavePendingTodos() {
        repository.deleteAll();
        repository.save(new Todo(null, "Todo Item 1", true, 1));
        repository.save(new Todo(null, "Todo Item 2", false, 2));
        repository.save(new Todo(null, "Todo Item 3", false, 3));
        assertThat(repository.getPendingTodos()).hasSize(2);
    }

    @Test
    void shouldGetPendingTodos() {
        List<Todo> pedingTodos = repository.getPendingTodos();
        List<String> todoNames = pedingTodos.stream()
                .map(Todo::getTitle)
                .toList();
        assertThat(pedingTodos).hasSize(3);
        assertThat(todoNames).contains("Buy groceries", "Finish SQL script");
    }

}
