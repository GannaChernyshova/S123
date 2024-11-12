package com.atomicjar.todos;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.testcontainers.Testcontainers;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(
        classes = Application.class,
        webEnvironment = RANDOM_PORT)
@Import(ContainersConfig.class)
public abstract class BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUpBase() {
        RestAssured.port = port;
        Testcontainers.exposeHostPorts(port);
    }
}
