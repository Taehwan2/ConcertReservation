package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.service.service.SeatService;
import com.example.concert.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserSeatFacade {

    private final UserService userService;

    private final  SeatService seatService;

    public List<ConcertSeat> getAbleSeats(Long concertDetailId) throws Exception {
        return seatService.FindAbleSeats(concertDetailId);
    }

    public ConcertSeat reserveSeats(ConcertSeatRequest concertSeatRequest) throws Exception {
        userService.getUserPoint(concertSeatRequest.getUserId());
        return seatService.reserveSeatTemp(concertSeatRequest);
    }
}
