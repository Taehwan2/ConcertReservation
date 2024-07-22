package com.example.concert.infrastructure.concert.reservtaion;

import com.example.concert.domain.reservation.entity.Reservation;
import com.example.concert.domain.reservation.service.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;
    //예약/결제 현황 저장
    @Override
    public Reservation save(Reservation reservation) {
        return reservationJpaRepository.save(reservation);
    }
}
