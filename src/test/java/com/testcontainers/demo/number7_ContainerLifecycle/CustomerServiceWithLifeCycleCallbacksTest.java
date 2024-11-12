package com.testcontainers.demo.number7_ContainerLifecycle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import com.testcontainers.demo.Customer;
import com.testcontainers.demo.CustomerService;
import com.testcontainers.demo.DBConnectionProvider;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;

public class CustomerServiceWithLifeCycleCallbacksTest {
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    CustomerService customerService;

    @BeforeAll
    static void startContainers() {
        postgres.start();
    }

    @AfterAll
    static void stopContainers() {
        postgres.stop();
    }

    @BeforeEach
    void setUp() {
        DBConnectionProvider connectionProvider = new DBConnectionProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        customerService = new CustomerService(connectionProvider);
        customerService.deleteAllCustomers();
    }

    @Test
    void shouldCreateCustomer() {
        customerService.createCustomer(new Customer(1L, "George Callback"));

        Optional<Customer> customer = customerService.getCustomer(1L);
        assertTrue(customer.isPresent());
        assertEquals(1L, customer.get().id());
        assertEquals("George Callback", customer.get().name());
    }

    @Test
    void shouldGetCustomers() {
        customerService.createCustomer(new Customer(1L, "George Callback"));
        customerService.createCustomer(new Customer(2L, "John Callback"));

        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size());
    }
}
