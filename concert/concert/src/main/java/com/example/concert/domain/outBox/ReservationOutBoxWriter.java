package com.example.concert.domain.outBox;

import com.example.concert.infrastructure.outbox.ReservationOutBox;
import com.example.concert.infrastructure.outbox.ReservationOutBoxStatus;

import java.util.List;

public interface ReservationOutBoxWriter {
    void complete(ReservationOutBox outBox);

    void saveOutBox(ReservationOutBox outBox);

    List<ReservationOutBox> findOutBoxFailed(ReservationOutBoxStatus outBoxStatus);

    ReservationOutBox findById(Long id);
}
