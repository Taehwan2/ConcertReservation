package com.example.concert.Presentation.consumer.reservation;

import com.example.concert.infrastructure.outbox.ReservationOutBox;
import com.example.concert.domain.outBox.ReservationOutBoxWriter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

@Service
@Slf4j
@Getter
@RequiredArgsConstructor
public class ReservationMessageConsumer {

    private CountDownLatch latch = new CountDownLatch(1);
    private final ReservationOutBoxWriter outBoxChange;

    @KafkaListener(topics = "exam-topic", groupId = "groupId", containerFactory = "kafkaListener")
    public void consume(@Payload ReservationOutBox outBox) throws IOException{
        log.info("Consumed message : {}",outBox.getId());
        outBoxChange.complete(outBox);

        latch.countDown();

    }

    public void resetLatch() {
        latch = new CountDownLatch(1);
    }

}
