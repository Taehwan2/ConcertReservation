package com.example.concert.Application;

import com.example.concert.Presentation.api.concert.model.seat.ConcertSeatRequest;
import com.example.concert.Presentation.api.point.model.PointRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.service.service.SeatService;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import com.example.concert.infrastructure.outbox.ReservationOutBox;
import com.example.concert.infrastructure.outbox.ReservationOutBoxStatus;
import com.example.concert.domain.queue.service.RedisQueueService;
import com.example.concert.domain.reservation.entity.Reservation;
import com.example.concert.domain.reservation.event.ReservationCompletedEvent;
import com.example.concert.domain.reservation.service.ReservationService;
import com.example.concert.domain.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ApplicationEventPublisher eventPublisher; // 이벤트 퍼블리셔 추가


   //실제 결제 로직
    @Transactional
    public Payment concertPayment(ConcertSeatRequest concertSeatRequest) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
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


        paymentSeat.forEach(concertSeat -> {
            Reservation reservation = Reservation.builder()
                    .seatId(concertSeat.getConcertSeatId())
                    .userId(userId)
                    .concertDetailId(concertSeatRequest.getConcertDetailId())
                    .concertId(concertDetail.getConcertId())
                    .build();

            reservationService.saveReservation(reservation);


            String json = null;
            try {
                json = objectMapper.writeValueAsString(reservation);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            //이벤트 발행 -> 카프카
            var outBox = ReservationOutBox.builder().id(reservation.getReservationId()).message(json).outBoxStatus(ReservationOutBoxStatus.INIT).build();
            eventPublisher.publishEvent(new ReservationCompletedEvent(this,outBox));
        });


        var pay = Payment.builder().check(true).build();
        //예약 반환.

        //redisQueueService.findExpiredAtAndUpdate2(userId);
        return pay;
    }




}
