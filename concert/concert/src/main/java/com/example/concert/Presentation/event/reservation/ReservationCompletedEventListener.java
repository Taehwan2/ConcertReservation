package com.example.concert.domain.reservation.event;

import com.example.concert.domain.outBox.OutBoxChange;
import com.example.concert.infrastructure.kafka.reservation.KafkaProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;



@Component
@RequiredArgsConstructor
public class ReservationCompletedEventListener {
    //private final ExternalApiClient externalApiClient;
    private final KafkaProducer kafkaProducer;
    private final OutBoxChange outBoxChange;
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutboxMessage(ReservationCompletedEvent event) throws InterruptedException {
       // kafkaProducer.sendMessage(event.getReservation(),event.getBoxId());
        outBoxChange.saveOutBox(event.getOutBox());
    }
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessage(ReservationCompletedEvent event){
        kafkaProducer.sendMessage(event.getOutBox());
    }
}
