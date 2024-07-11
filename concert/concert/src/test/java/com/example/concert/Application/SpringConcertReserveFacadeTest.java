package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringConcertReserveFacadeTest {

    private ConcertReserveFacade concertReserveFacade;
    @Test
    @DisplayName("미리 DB에 값넣고 테스트")
    void concertPaymenTest() throws Exception {
        var request = new ConcertSeatRequest(1L,1L,10);
        System.out.println(concertReserveFacade.concertPayment(request));
    }
}