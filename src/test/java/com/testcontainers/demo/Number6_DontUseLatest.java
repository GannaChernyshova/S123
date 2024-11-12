package com.testcontainers.demo;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MongoDBContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Number6_DontUseLatest {

    static public MongoDBContainer mongoContainer = new MongoDBContainer("mongo:latest"); // Use MongoDB >= 4.4 for transaction support

    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> collection;

    @BeforeAll
    public static void setUpAll() {
        mongoContainer.start();
    }

    @AfterAll
    public static void tearDown() {
        mongoContainer.stop();
    }

    @BeforeEach
    public void setUp() {
        mongoClient = new MongoClient(new MongoClientURI(mongoContainer.getReplicaSetUrl()));
        database = mongoClient.getDatabase("testdb");
        collection = database.getCollection("tasks");
    }

    @Test
    public void testInsertWithTransaction() {
        // Start a client session to use transactions
        try (ClientSession session = mongoClient.startSession()) {
            session.startTransaction();
            collection.insertOne(session, new Document("title", "Task 1"));
            collection.insertOne(session, new Document("title", "Task 2"));
            collection.insertOne(session, new Document("title", "Task 3"));
            session.commitTransaction();
        }
        long count = collection.countDocuments();
        assertEquals(3, count, "Expected 3 documents in the collection after the transaction");
    }
//    MongoDB 3.6: Since MongoDB 3.6 does not support transactions,
//    attempting to start a transaction will fail, and no documents will be inserted.
//    This will cause the test to fail because the final count will not match the expected value of 3.

}
