package com.example.concert.domain.reservation.event;

import com.example.concert.domain.reservation.entity.Reservation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Getter
public class ReservationCompletedEvent extends ApplicationEvent {
    private final Reservation reservation;

    public ReservationCompletedEvent(Object source, Reservation reservation) {
        super(source);
        this.reservation = reservation;
    }
}
