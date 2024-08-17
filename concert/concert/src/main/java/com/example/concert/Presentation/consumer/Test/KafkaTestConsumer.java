package com.example.concert.Presentation.consumer.Test;


import com.example.concert.infrastructure.outbox.ReservationOutBox;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@Getter
public class KafkaTestConsumer {

    private CountDownLatch latch = new CountDownLatch(1);
    private String message;

    @KafkaListener(topics = "TEST_TOPIC",groupId = "foo", containerFactory = "factory2")
    public void consume(@Payload String message) throws IOException {
        log.info("Consumed message : {}",message);
        this.message = message;
        latch.countDown();
    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

}
