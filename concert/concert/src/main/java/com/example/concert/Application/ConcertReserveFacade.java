package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.reservation.entity.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ConcertReserveFacade {
    public Reservation concertPayment(ConcertSeatRequest concertSeatRequest) {
    return new Reservation();
    }
}
