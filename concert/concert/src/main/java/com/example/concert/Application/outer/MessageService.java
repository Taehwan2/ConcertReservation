package com.example.concert.Application.outer;

import org.springframework.stereotype.Service;

@Service
public class MessageService {
    void send(Long userId) throws InterruptedException {
        Thread.sleep(3000L); // 3초 정지
        if (userId > 0) throw new RuntimeException();
    }
}
