package com.example.concert.domain.outBox;

import com.example.concert.infrastructure.outbox.ReservationOutBox;

public interface ReservationMessageProducer {
    void sendMessage(ReservationOutBox reservation);
}
