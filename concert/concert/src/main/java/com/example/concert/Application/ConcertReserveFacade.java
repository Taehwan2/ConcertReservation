package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.Presentation.point.model.PointResponse;
import com.example.concert.domain.concert.service.ConcertService;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.service.service.SeatService;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import com.example.concert.domain.queue.service.RedisQueueService;
import com.example.concert.domain.reservation.entity.Reservation;
import com.example.concert.domain.reservation.service.ReservationService;
import com.example.concert.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertReserveFacade {
    private final SeatService seatService;
    private final UserService userService;
    private final ReservationService reservationService;
    private final ConcertDetailService concertDetailService;
    private final RedisQueueService redisQueueService;
   //실제 결제 로직
    public Payment concertPayment(ConcertSeatRequest concertSeatRequest) throws Exception {
        // 임시 예약좌석 불러오기.
        Long userId = concertSeatRequest.getUserId();
        ConcertDetail concertDetail = concertDetailService.getConcertDetail(concertSeatRequest.getConcertDetailId());
        List<ConcertSeat> tempSeats = seatService.findTempSeatByUserId(userId);
        BigDecimal payment = tempSeats.stream().map(ConcertSeat::getPrice).reduce(BigDecimal.ZERO,BigDecimal::add);
        //결제.
        PointRequest pointRequest = new PointRequest(userId,payment);
        var userPoint = userService.getUserPoint(pointRequest.getUserId());
        userPoint.calculate(payment);
        userService.save(userPoint);

        //예약좌석 업데이트.
        List<ConcertSeat> paymentSeat = seatService.updatedSeatToReserved(userId,tempSeats);

        paymentSeat.forEach(
                concertSeat -> {
                  reservationService.saveReservation(
                          Reservation.builder()
                                  .seatId(concertSeat.getConcertSeatId())
                                  .userId(userId)
                                  .concertDetailId(concertSeatRequest.getConcertDetailId())
                                  .concertId(concertDetail.getConcertId())
                                  .build()
                  );
                }
        );
        //예약저장.

        var pay = Payment.builder().check(true).build();
        //예약 반환.

        redisQueueService.findExpiredAtAndUpdate2(userId);
        return pay;
    }
}
