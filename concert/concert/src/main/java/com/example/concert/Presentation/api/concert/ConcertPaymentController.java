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
    //결제 로직 api를 담은 컨트롤러
    private final ConcertReserveFacade concertReserveFacade;
    private final ReservationMapper reservationMapper;
   //예약된 좌석을 구매하고 결제하는 서비스
    @PostMapping("/concert/reservation/days")
    public Payment payment(@RequestBody ConcertSeatRequest concertSeatRequest) throws Exception {
        var payment  =  concertReserveFacade.concertPayment(concertSeatRequest);
        return payment;
    }
}
