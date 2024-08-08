package com.example.concert.Application.outer;

import com.example.concert.domain.reservation.entity.Reservation;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ExternalApiClient {

    public void sendOrderResult(Reservation reservation) throws InterruptedException {

        Thread.sleep(3000L); // 3초 정지
        System.out.println("Sending order result to external API for reservation: " + reservation.getReservationId());
        throw new RuntimeException();
    }
}
