package com.example.concert.domain.concertSeat.service.service;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
//좌석 통합 테스트
@SpringBootTest
class SpringSeatServiceTest {

    @Autowired
    private SeatService seatService;

    // 실제로 콘서트 좌석을 넣은후 가능한 좌석이있는지 확인하는 테스트
    @Test
    void findAbleSeats() throws Exception {
        var request = new ConcertSeatRequest(1L,1L,1);
        var result = seatService.reserveSeatTemp(request);
        var list = seatService.FindAbleSeats(1L);
        System.out.println(list.get(1).getSeatNo());
    }

    @Test
    void getAbleSeats() {
    }

    @Test
    void checkSize() {
    }
   //사실 상 로직을 다 타는 건 불가능하고 미리 데이터를 만들고 좌석을 저장하는 테스트
    @Test
    void reserveSeatTempTest() throws Exception {

            for (int i = 1; i < 500; i++) {
                var request = new ConcertSeatRequest(1L, 1L, i);
                var result = seatService.reserveSeatTemp(request);
            }
            for (int i = 1; i < 500; i++) {
                var request = new ConcertSeatRequest(3L, 2L, i);
                var result = seatService.reserveSeatTemp(request);
            }
            for (int i = 1; i < 500; i++) {
                var request = new ConcertSeatRequest(2L, 3L, i);
                var result = seatService.reserveSeatTemp(request);
            }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(4L, 4L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(6L, 5L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(5L, 6L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(7L, 7L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(8L, 8L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(9L, 9L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(10L, 10L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(11L, 11L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(12L, 12L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(13L, 13L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(14L, 14L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(15L, 15L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(16L, 16L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(17L, 17L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(18L, 18L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(19L, 19L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(20L, 20L, i);
            var result = seatService.reserveSeatTemp(request);
        }
        for (int i = 1; i < 500; i++) {
            var request = new ConcertSeatRequest(21L, 21L, i);
            var result = seatService.reserveSeatTemp(request);
        }


    }

    @Test
    void checkExpiredSeat() {
    }
}