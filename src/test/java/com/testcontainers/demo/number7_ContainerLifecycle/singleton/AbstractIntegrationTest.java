package com.testcontainers.demo.number7_ContainerLifecycle.singleton;

import com.testcontainers.demo.CustomerService;
import com.testcontainers.demo.DBConnectionProvider;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;


abstract class AbstractIntegrationTest {


    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:16-alpine"));

    static {
        postgres.start();
    }

    static DBConnectionProvider connectionProvider;
    static CustomerService customerService;

    @BeforeAll
    static void setUp() {
        connectionProvider = new DBConnectionProvider(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );
        customerService = new CustomerService(connectionProvider);
    }

    @BeforeEach
    void setUpBeforeEach() {
        System.out.println(postgres.getContainerName());
    }
}