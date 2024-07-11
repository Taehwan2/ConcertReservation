package com.example.concert.domain.reservation.service;

import com.example.concert.domain.reservation.entity.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);
}
