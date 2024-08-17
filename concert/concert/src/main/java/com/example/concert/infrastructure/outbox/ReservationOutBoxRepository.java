package com.example.concert.infrastructure.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationOutBoxRepository extends JpaRepository<ReservationOutBox,Long> {

    @Query("select c from ReservationOutBox  c where  c.outBoxStatus =:status")
    List<ReservationOutBox> findOutBoxesByOutBoxStatus(@Param("status") ReservationOutBoxStatus outBoxStatus );

}
