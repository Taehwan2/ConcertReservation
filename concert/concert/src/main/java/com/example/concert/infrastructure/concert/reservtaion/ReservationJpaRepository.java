package com.example.concert.infrastructure.concert.reservtaion;

import com.example.concert.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {

}
