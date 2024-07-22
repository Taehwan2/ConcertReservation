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
        var request = new ConcertSeatRequest(1L,1L,1);
        var result = seatService.reserveSeatTemp(request);
        System.out.println(result.getSeatNo());
        System.out.println(result.getSeatStatus());
    }

    @Test
    void checkExpiredSeat() {
    }
}