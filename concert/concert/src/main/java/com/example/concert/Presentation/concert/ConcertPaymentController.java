package com.example.concert.Presentation.concert;

import com.example.concert.Application.ConcertReserveFacade;
import com.example.concert.Application.Payment;
import com.example.concert.Presentation.concert.mapper.ReservationMapper;
import com.example.concert.Presentation.concert.model.reservation.ReservationResponse;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.Presentation.concert.swaggerController.ConcertPaymentControllerDocs;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ConcertPaymentController implements ConcertPaymentControllerDocs {

    private final ConcertReserveFacade concertReserveFacade;
    private final ReservationMapper reservationMapper;

    @PostMapping("/concert/reservation/days/{concertId}")
    public Payment payment(@RequestBody ConcertSeatRequest concertSeatRequest) throws Exception {
        var payment  =  concertReserveFacade.concertPayment(concertSeatRequest);
        return payment;
    }
}
