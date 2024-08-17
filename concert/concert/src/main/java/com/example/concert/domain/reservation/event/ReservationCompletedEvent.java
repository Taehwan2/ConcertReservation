package com.example.concert.domain.reservation.event;

import com.example.concert.infrastructure.outbox.ReservationOutBox;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;


@Getter
public class ReservationCompletedEvent extends ApplicationEvent {
    //private final Reservation reservation;
    private final ReservationOutBox outBox;
    public ReservationCompletedEvent(Object source, ReservationOutBox outBox) {
        super(source);
        //this.reservation = reservation;
        this.outBox = outBox;
    }
}
