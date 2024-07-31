package com.example.concert.currency;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import com.example.concert.domain.concertSeat.service.service.SeatRepository;
import com.example.concert.domain.concertSeat.service.service.SeatService;
import com.example.concert.infrastructure.concert.seat.SeatJpaRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@SpringBootTest
@EnableRetry
public class ConcertSeatServiceTest {
    //10개의 쓰레드를 사용해서 동시성을 테스트하는 코드
    @Autowired
    private SeatService concertSeatService;


    @Autowired
    private SeatJpaRepository seatRepository;

    private static final int THREAD_COUNT = 100;
    //비관적 Lock은 insert에 존재하지 않음으로. 좌석이 있다는 가정하에 비관적락 테스트
/*   @BeforeEach
    void before(){
<<<<<<< Updated upstream
        seatRepository.save(ConcertSeat.builder().concertDetailId(1L).concertSeatId(1L).seatNo(1).version(1).seatStatus(SeatStatus.RESERVABLE).build());
    }
       @Test
=======
        seatRepository.save(ConcertSeat.builder().concertDetailId(1L).concertSeatId(1L).seatNo(1).seatStatus(SeatStatus.RESERVABLE).build());
    }*/
    @Test
>>>>>>> Stashed changes
    @DisplayName("이미 좌석이 예약되어있는경우 비관적Lock으로 조회하고 update로직을 실행하여 한 트랜잭션이 성공하면 아래는 모두 update를 실패한다.")
    public void testConcurrentSeatReservation() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        Long concertDetailId = 1L;
        int seatNo = 1;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < THREAD_COUNT; i++) {
            Long userId = (long) i + 1;
            executorService.submit(() -> {
                try {
                    ConcertSeatRequest request = new ConcertSeatRequest(userId, concertDetailId, seatNo);
                    var result = concertSeatService.reserveSeatTemp(request);
                    System.out.println(result.getSeatStatus());
                    System.out.println(result.getUserId());
                    System.out.println(result.getSeatNo());
                } catch (OptimisticLockingFailureException e) {
                    System.out.println("낙관적락에 걸림");
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
                latch.await();
                executorService.shutdown();
                long endTime = System.currentTimeMillis(); // 종료 시간 측정
                System.out.println("Execution time: " + (endTime - startTime) + " ms"); // 실행 시간 출력

            }

}
    @DisplayName("[좌석이 없는 경우] - 동시에 여러명이 한 좌석을 예약하는 경우 1명만 예약이 되어야 한다.")
    @Test
    void test_temporaryReservationSeat_notRow() throws InterruptedException {
        // Given
        Long concertDetailId = 2L;
        int seatNo = 1;

        // When
        int numberOfRequest = 10;
        CountDownLatch latch = new CountDownLatch(numberOfRequest);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfRequest);

        for(int i = 0; i < numberOfRequest; i++){
            Long userId = (long) i;
            executorService.submit(() -> {
                try {
                    ConcertSeatRequest request = new ConcertSeatRequest(userId, concertDetailId, seatNo);
                    concertSeatService.reserveSeatTemp(request);
                } catch (Exception e){

                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();


    }
}
