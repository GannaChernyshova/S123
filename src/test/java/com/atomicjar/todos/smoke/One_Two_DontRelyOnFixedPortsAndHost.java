package com.atomicjar.todos.smoke;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import static org.assertj.core.api.Assertions.assertThat;


public class One_Two_DontRelyOnFixedPortsAndHost {

    static GenericContainer<?> redis =
            new GenericContainer<>("redis:5.0.3-alpine")
                    .withExposedPorts(6379);

    private static JedisPool jedisPool;

    @BeforeAll
    public static void setUp() {
        redis.start();
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost",6379);

    }

    @AfterAll
    public static void tearDown() {
        // Assume that we have Redis running locally
        jedisPool.close();
        redis.stop();

    }

    @Test
    public void testSimplePutAndGet() {
        Jedis jedis = jedisPool.getResource();
        jedis.set("mykey", "Hello from Jedis");

        String value = jedis.get("mykey");
        System.out.println(value);

        assertThat(value).isEqualTo("Hello from Jedis");
    }
}
