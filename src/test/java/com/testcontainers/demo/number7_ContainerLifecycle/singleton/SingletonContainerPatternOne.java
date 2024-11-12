package com.testcontainers.demo.number7_ContainerLifecycle.singleton;

import com.testcontainers.demo.Customer;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SingletonContainerPatternOne extends AbstractIntegrationTest {

    @Test
    void shouldCreateCustomer() {
        customerService.deleteAllCustomers();
        customerService.createCustomer(new Customer(1L, "George Singleton First"));

        Optional<Customer> customer = customerService.getCustomer(1L);
        assertTrue(customer.isPresent());
        assertEquals(1L, customer.get().id());
        assertEquals("George Singleton First", customer.get().name());
    }

    @Test
    void shouldGetCustomers() {
        customerService.deleteAllCustomers();
        customerService.createCustomer(new Customer(1L, "George Singleton First"));
        customerService.createCustomer(new Customer(2L, "John Singleton First"));

        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size());
    }
}
