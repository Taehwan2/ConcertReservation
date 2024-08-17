package com.example.concert.Presentation.event.reservation;

import com.example.concert.domain.outBox.ReservationMessageProducer;
import com.example.concert.domain.outBox.ReservationOutBoxWriter;
import com.example.concert.domain.reservation.event.ReservationCompletedEvent;
import com.example.concert.infrastructure.kafka.reservation.ReservationMessageProducerImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;



@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationCompletedEventListener {
    //private final ExternalApiClient externalApiClient;
    private final ReservationMessageProducer kafkaProducer;
    private final ReservationOutBoxWriter outBoxChange;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutboxMessage(ReservationCompletedEvent event) throws InterruptedException {
        log.info("creatOutBox");
        outBoxChange.saveOutBox(event.getOutBox());
    }
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)

    public void sendMessage(ReservationCompletedEvent event){
        log.info("updateOutBox");
        kafkaProducer.sendMessage(event.getOutBox());
    }
}
