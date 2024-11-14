package com.testcontainers.demo;

import org.junit.Rule;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Number4_DontUseSleep {

    public GenericContainer redis = new GenericContainer<>("redis:7.4.1")
            .withExposedPorts(6379)
            .waitingFor(Wait.forLogMessage(".*Ready to accept connections.*\\n", 1));

    @Rule
    public GenericContainer<?> nginx = new GenericContainer<>("nginx:1.27.0-alpine3.19-slim")
            .withExposedPorts(80)
            .dependsOn(redis);

    private JedisPool jedisPool;

    @Test
    public void testSimplePutAndGet() throws InterruptedException {
        redis.start();

        nginx.start();

        jedisPool = new JedisPool(new JedisPoolConfig(), redis.getHost(), redis.getMappedPort(6379));

        Jedis jedis = jedisPool.getResource();
        //put
        jedis.set("mykey", "Hello from Jedis");
        //get
        String value = jedis.get("mykey");
        System.out.println(value);
        assertThat(value).isEqualTo("Hello from Jedis");
    }
}
