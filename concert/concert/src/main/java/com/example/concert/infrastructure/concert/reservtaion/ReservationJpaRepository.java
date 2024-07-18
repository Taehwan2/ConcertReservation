package com.example.concert.infrastructure.concert.reservtaion;

import com.example.concert.domain.reservation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
//jpa 구현체
public interface ReservationJpaRepository extends JpaRepository<Reservation,Long> {

}
