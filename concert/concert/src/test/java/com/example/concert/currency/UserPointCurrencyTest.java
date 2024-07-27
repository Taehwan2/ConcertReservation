package com.example.concert.currency;

import com.example.concert.Application.UserPointFacade;
import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.retry.annotation.EnableRetry;

import java.math.BigDecimal;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@EnableRetry
public class UserPointCurrencyTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserPointFacade userPointFacade;

    private static final int THREAD_COUNT = 10;

    @BeforeEach
    void beforeEach() throws Exception {
        userService.save(new User(1L,"taehwan",new BigDecimal(1000)));
    }

    @Test
    @DisplayName("쓰레드를 10개놓고 같은 userId에 동시에 5000원을 충전했을때 51000원이 나오는 로직")
    public void testConcurrentSeatReservation() throws InterruptedException {
        System.out.println(userService.getUserPoint(1L).getName());
        ExecutorService executorService = Executors.newFixedThreadPool(THREAD_COUNT);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        int random = new Random().nextInt(10000);

        long startTime = System.currentTimeMillis();
        for (int i = 0; i < THREAD_COUNT; i++) {
            executorService.submit(() -> {
                try {
                    userPointFacade.changePoint(new PointRequest(1L,new BigDecimal(5000)));
                } catch (OptimisticLockingFailureException e){//낙관적 락을 사용했을 때 로직
                    System.out.println("여기걸림");
                } catch (Exception e) {
                    System.out.println("Exception: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        System.out.println(userService.getUserPoint(1L).getPoint());

        long endTime = System.currentTimeMillis(); // 종료 시간 측정
        System.out.println("Execution time: " + (endTime - startTime) + " ms"); // 실행 시간 출력
    }
}
