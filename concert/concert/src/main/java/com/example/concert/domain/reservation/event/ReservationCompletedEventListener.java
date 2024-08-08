package com.example.concert.domain.reservation.event;

import com.example.concert.Application.outer.ExternalApiClient;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;



@Component
@RequiredArgsConstructor
public class ReservationCompletedEventListener {
    private final ExternalApiClient externalApiClient;
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    //@EventListener
    public void handleReservationCompletedEvent(ReservationCompletedEvent event) throws InterruptedException {

        System.out.println("Handling reservation completed event for reservation: " + event.getReservation().getReservationId());
        externalApiClient.sendOrderResult(event.getReservation());
    }
}
