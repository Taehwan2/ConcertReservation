package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.service.QueueService;
import com.example.concert.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserQueueFacade {

    private final UserService userService;
    private final QueueService queueService;

    public Queue getQueue(Long userId, Long waitId) throws Exception {
        userService.getUserPoint(userId);
        return queueService.getQueue(userId);
    }

    public Queue enrollQueue(QueueRequest queueRequest) throws Exception {
        userService.getUserPoint(queueRequest.getUserId());
        return queueService.registerQueue(queueRequest);
    }
}
