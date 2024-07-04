package com.example.concert.Presentation.concert.mapper;

import com.example.concert.Presentation.concert.model.reservation.ReservationResponse;
import com.example.concert.domain.reservation.entity.Reservation;
import org.springframework.stereotype.Service;

@Service
public class ReservationMapper {
    public ReservationResponse entityToResponse(Reservation reservation){
        return new ReservationResponse();
    }

}
