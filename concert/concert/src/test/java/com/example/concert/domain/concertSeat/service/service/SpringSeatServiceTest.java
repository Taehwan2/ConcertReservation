package com.example.concert.domain.concertSeat.service.service;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringSeatServiceTest {

    @Autowired
    private SeatService seatService;
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