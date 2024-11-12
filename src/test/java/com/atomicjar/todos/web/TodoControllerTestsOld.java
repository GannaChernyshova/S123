package com.atomicjar.todos.web;

import com.atomicjar.todos.Application;
import com.atomicjar.todos.BaseIntegrationTest;
import com.atomicjar.todos.repository.TodoRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = Application.class,
        webEnvironment = RANDOM_PORT)
@Testcontainers
@Sql("/test-data.sql")
public class TodoControllerTestsOld {

    @Container
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Autowired
    TodoRepository todoRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUpBase() {
        RestAssured.port = port;
        org.testcontainers.Testcontainers.exposeHostPorts(port);
    }

    @Test
    void shouldGetAllTodos() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos")
                .then()
                .statusCode(200)
                .body(".", hasSize(5));
    }

    @Test
    void shouldGetTodoById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/todos/{id}", 1)
                .then()
                .statusCode(200)
                .body("title", is("Buy groceries"))
                .body("completed", is(false))
                .body("order", is(1));
    }

    @Test
    void shouldCreateTodoSuccessfully() {
        given()
                .contentType(ContentType.JSON)
                .body(
                    """
                    {
                        "title": "Todo Item 6",
                        "completed": false,
                        "order": 6
                    }
                    """
                )
                .when()
                .post("/todos")
                .then()
                .statusCode(201)
                .body("title", is("Todo Item 6"))
                .body("completed", is(false))
                .body("order", is(6));
    }

    @Test
    void shouldDeleteTodoById() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/todos/{id}", 5)
                .then()
                .statusCode(200);

        assertThat(todoRepository.findById("5")).isEmpty();
    }
}
