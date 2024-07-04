package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserSeatFacade {
    public List<ConcertSeat> getAbleSeats(Long concertDetailId) {
        return List.of();
    }

    public ConcertSeat reserveSeats(ConcertSeatRequest concertSeatRequest) {
        return new ConcertSeat();
    }
}
