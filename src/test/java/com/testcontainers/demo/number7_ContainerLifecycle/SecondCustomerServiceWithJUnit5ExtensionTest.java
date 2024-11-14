package com.testcontainers.demo.number7_ContainerLifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import com.testcontainers.demo.Customer;
import com.testcontainers.demo.CustomerService;
import com.testcontainers.demo.DBConnectionProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class SecondCustomerServiceWithJUnit5ExtensionTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:16-alpine"
    );

    CustomerService customerService;

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
        System.out.println(postgres.getContainerName());
        customerService.createCustomer(new Customer(1L, "George JUnit5"));

        Optional<Customer> customer = customerService.getCustomer(1L);
        assertTrue(customer.isPresent());
        assertEquals(1L, customer.get().id());
        assertEquals("George JUnit5", customer.get().name());
    }

    @Test
    void shouldGetCustomers() {
        System.out.println(postgres.getContainerName());
        customerService.createCustomer(new Customer(1L, "George JUnit5"));
        customerService.createCustomer(new Customer(2L, "John JUnit5"));

        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size());
    }
}