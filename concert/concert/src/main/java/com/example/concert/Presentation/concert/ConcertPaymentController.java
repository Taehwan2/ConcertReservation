package com.example.concert.Presentation.concert;

import com.example.concert.Application.ConcertReserveFacade;
import com.example.concert.Presentation.concert.mapper.ReservationMapper;
import com.example.concert.Presentation.concert.model.reservation.ReservationResponse;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConcertPaymentController {

    private final ConcertReserveFacade concertReserveFacade;
    private final ReservationMapper reservationMapper;

    @PostMapping("/concert/reservation/days/{concertId}")
    public ReservationResponse payment(@RequestBody ConcertSeatRequest concertSeatRequest){
        var dates  =  concertReserveFacade.concertPayment(concertSeatRequest);
        return reservationMapper.entityToResponse(dates);
    }
}
