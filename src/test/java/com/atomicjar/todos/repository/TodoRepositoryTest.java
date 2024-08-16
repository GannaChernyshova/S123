package com.atomicjar.todos.repository;

import com.atomicjar.todos.entity.Todo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:tc:postgresql:15-alpine:///todos"
})
class TodoRepositoryTest {

    @Autowired
    TodoRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    void shouldGetPendingTodos() {
        repository.save(new Todo(null, "Todo Item 1", true, 1));
        repository.save(new Todo(null, "Todo Item 2", false, 2));
        repository.save(new Todo(null, "Todo Item 3", false, 3));
        assertThat(repository.getPendingTodos()).hasSize(2);
    }

    @Test
    void shouldSaveTodo() {
        repository.save(new Todo("one", "Todo Item 1", false, 1));
        assertThat(repository.getPendingTodos().get(0).getTitle()).isEqualTo("Todo Item 1");
        assertThat(repository.getPendingTodos().get(0).getCompleted()).isFalse();
    }

    @Test
    void shouldDeleteTodoById() {
        repository.save(new Todo("one", "Todo Item 1", false, 1));
        repository.save(new Todo("two", "Todo Item 2", false, 2));
        repository.deleteAll();
        assertThat(repository.getPendingTodos()).hasSize(0);
    }

}