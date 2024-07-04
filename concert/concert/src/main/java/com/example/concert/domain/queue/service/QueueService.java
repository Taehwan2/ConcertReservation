package com.example.concert.domain.queue.service;

import com.example.concert.domain.queue.entitiy.Queue;
import org.springframework.stereotype.Service;


@Service
public class QueueService {
    public Queue getQueue(Long userId, Long waitId) {
        return new Queue();
    }
}
