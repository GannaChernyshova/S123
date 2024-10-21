package com.atomicjar.todos.smoke;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testcontainers.containers.GenericContainer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static org.assertj.core.api.Assertions.assertThat;

public class Three_DontHardcodeContainerName {

    GenericContainer<?> redis =
            new GenericContainer<>("redis:5.0.3-alpine")
                    .withExposedPorts(6379)
                    .withCreateContainerCmdModifier(cmd -> cmd.withName("redis"));

    private  JedisPool jedisPool;

    @BeforeEach
    public  void setUp() {
        // Assume that we have Redis running locally
        redis.start();
        jedisPool = new JedisPool(new JedisPoolConfig(), redis.getHost(), redis.getMappedPort(6379));

    }

    @AfterEach
    public  void tearDown() {
        // Assume that we have Redis running locally
        jedisPool.close();
        redis.stop();

    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testSimplePutAndGetTwo() {
        Jedis jedis = jedisPool.getResource();
        jedis.set("mykey", "Hello from Jedis2");

        String value = jedis.get("mykey");
        System.out.println(value);

        assertThat(value).isEqualTo("Hello from Jedis2");
    }

    @Test
    @Execution(ExecutionMode.CONCURRENT)
    public void testSimplePutAndGetTwo2() {
        Jedis jedis = jedisPool.getResource();
        jedis.set("keyvalue", "Do not hardcode the hostname");

        String value = jedis.get("keyvalue");
        System.out.println(value);

        assertThat(value).isEqualTo("Do not hardcode the hostname");
    }
}
