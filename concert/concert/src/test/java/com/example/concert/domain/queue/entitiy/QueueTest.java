package com.example.concert.domain.queue.entitiy;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class QueueTest {

    @Test
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
    void alreadyWaitTest() {
        Queue queue = Queue.builder()
                .userId(1L)
                .waitingNumber(5)
                .userStatus(UserStatus.WAITING)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        Exception exception = assertThrows(Exception.class, queue::alreadyWait);
        assertEquals("already wait", exception.getMessage());
    }

    @Test
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