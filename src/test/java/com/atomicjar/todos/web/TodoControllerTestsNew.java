package com.atomicjar.todos.web;

import com.atomicjar.todos.BaseIntegrationTest;
import com.atomicjar.todos.repository.TodoRepository;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;

@Sql("/test-data.sql")
public class TodoControllerTestsNew extends BaseIntegrationTest {

    @Autowired
    TodoRepository todoRepository;

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
