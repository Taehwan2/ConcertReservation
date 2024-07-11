package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.service.service.SeatService;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import com.example.concert.domain.reservation.entity.Reservation;
import com.example.concert.domain.reservation.service.ReservationService;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ConcertReserveFacadeTest {
    @Mock
    private ConcertDetailService concertDetailService;

    @Mock
    private SeatService seatService;

    @Mock
    private UserService userService;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ConcertReserveFacade concertReserveFacade;

    @Test
    void concertPayment() throws Exception {
        Long userId = 1L;
        Long concertDetailId = 1L;
        ConcertSeatRequest concertSeatRequest = new ConcertSeatRequest(userId, concertDetailId,3);
        ConcertDetail concertDetail = new ConcertDetail();


        ConcertSeat seat1 = new ConcertSeat();
        seat1.setPrice(new BigDecimal("50.00"));
        ConcertSeat seat2 = new ConcertSeat();
        seat2.setPrice(new BigDecimal("75.00"));

        List<ConcertSeat> tempSeats = List.of(seat1, seat2);

        User userPoint = User.builder().userId(1L).point(new BigDecimal(1000)).build();

        given(concertDetailService.getConcertDetail(concertDetailId)).willReturn(concertDetail);
        given(seatService.findTempSeatByUserId(userId)).willReturn(tempSeats);
        given(userService.getUserPoint(userId)).willReturn(userPoint);
        given(seatService.updatedSeat(userId, tempSeats)).willReturn(tempSeats);

        // When
        Payment payment = concertReserveFacade.concertPayment(concertSeatRequest);

        // Then
        then(concertDetailService).should().getConcertDetail(concertDetailId);
        then(seatService).should().findTempSeatByUserId(userId);
        then(userService).should().getUserPoint(userId);
        then(userService).should().save(userPoint);
        then(seatService).should().updatedSeat(userId, tempSeats);


        assertEquals(true, payment.isCheck());
    }
    }
