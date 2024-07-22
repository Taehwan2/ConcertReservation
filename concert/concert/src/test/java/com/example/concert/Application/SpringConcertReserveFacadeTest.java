package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringConcertReserveFacadeTest {
   // 결제 서비스 통합 테스트
    private ConcertReserveFacade concertReserveFacade;
    @Test
    @DisplayName("미리 DB에 값넣고 테스트")
    void concertPaymenTest() throws Exception {
        //미리 데이터 베이스에 값을 넣고 테스트
        var request = new ConcertSeatRequest(1L,1L,10);
        System.out.println(concertReserveFacade.concertPayment(request));
    }
}