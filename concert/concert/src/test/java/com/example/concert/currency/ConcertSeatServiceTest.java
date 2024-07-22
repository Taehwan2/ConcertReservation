package com.example.concert.currency;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import com.example.concert.domain.concertSeat.service.service.SeatRepository;
import com.example.concert.domain.concertSeat.service.service.SeatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@SpringBootTest
public class ConcertSeatServiceTest {
    //10개의 쓰레드를 사용해서 동시성을 테스트하는 코드
    @Autowired
    private SeatService concertSeatService;

    @Autowired
    private SeatRepository seatRepository;

    private static final int THREAD_COUNT = 10;

    @Test
    public void testConcurrentSeatReservation() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        Long concertDetailId = 1L;
        int seatNo = 1;

        for (int i = 0; i < THREAD_COUNT; i++) {
            Long userId = (long) i + 1;
            executorService.submit(() -> {
                try {
                    ConcertSeatRequest request = new ConcertSeatRequest(userId, concertDetailId, seatNo);
                    concertSeatService.reserveSeatTemp(request);
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        ConcertSeat reservedSeat = seatRepository.findSeat(concertDetailId, seatNo);
    }
}