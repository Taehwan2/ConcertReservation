package com.example.concert.kafka;


import com.example.concert.Presentation.config.KafkaConfiguration;
import com.example.concert.Presentation.config.KafkaConsumerConfig;
import com.example.concert.Presentation.consumer.Test.KafkaTestConsumer;
import com.example.concert.infrastructure.kafka.Test.KafkaTestProducer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class KafkaIntegrationTest {

    @Autowired
    private KafkaTestProducer producer;

    @Autowired
    private KafkaTestConsumer consumer;

    @Test
    void testKafkaMessageSendAndReceive() throws InterruptedException {
        producer.sendMessage("test-message");
        System.out.println("보냄");


        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertThat(consumer.getMessage()).isEqualTo("test-message");
    }


}