package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import org.springframework.stereotype.Service;

@Service
public class UserQueueFacade {
    public Queue getQueue(Long userId, Long waitId) {
        return new Queue();
    }

    public Queue enrollQueue(QueueRequest queueRequest) {
        return new Queue();
    }
}
