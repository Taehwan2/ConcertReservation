package com.example.concert.domain.queue.entitiy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {
//대기열 도메인 테스트
    @Test
    @DisplayName("순서 갱신하는 도메인 로직 10으로 업데이트")
    void updateWaitingNumberTest() {
        Queue queue = Queue.builder()
                .userId(1L)
                .waitingNumber(5)
                .userStatus(UserStatus.WAITING)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        queue.updateWaitingNumber(10);
        assertEquals(10, queue.getWaitingNumber());
    }



    @Test
    @DisplayName("이미 실행중임의 예외를 반환하는 테스트")
    void alreadyWorkingTest() {
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(10);
        Queue queue = Queue.builder()
                .userId(1L)
                .waitingNumber(5)
                .userStatus(UserStatus.WORKING)
                .expiredAt(expiryTime)
                .build();

        Exception exception = assertThrows(Exception.class, queue::alreadyWorking);
        assertEquals("already working it will be expired at [" + expiryTime + "]", exception.getMessage());
    }

    @Test
    @DisplayName("대기 상태로 바꿔주는 테스트")
    void setWaitTest() {
        Queue queue = Queue.builder()
                .userId(1L)
                .waitingNumber(5)
                .userStatus(UserStatus.WORKING)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        queue.setWait();
        assertEquals(UserStatus.WAITING, queue.getUserStatus());
    }

    @Test
    @DisplayName("실행중과 만료시간 30분을 주는 테스트")
    void setWorkingTest() {
        Queue queue = Queue.builder()
                .userId(1L)
                .waitingNumber(5)
                .userStatus(UserStatus.WAITING)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        queue.setWorking(30);
        assertEquals(UserStatus.WORKING, queue.getUserStatus());
    }
}