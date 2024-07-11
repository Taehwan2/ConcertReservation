package com.example.concert.domain.reservation.service;

import com.example.concert.domain.reservation.entity.Reservation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public Reservation saveReservation(Reservation reservation){
        return  reservationRepository.save(reservation);
    }

}
