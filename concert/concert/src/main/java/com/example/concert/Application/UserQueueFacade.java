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
 //user와 queue의 기능을 담은 Servie 가져오기
    private final UserService userService;
    private final QueueService queueService;

    public Queue getQueue(Long userId,Long waitId) throws Exception {
        userService.getUserPoint(userId);   //존재하는 유저인지 검증하는 서비스 호출
        return queueService.getQueue(waitId);  //대기열에서 유저의 대기번호를 가져오는 서비스 호출
    }

    public Queue enrollQueue(QueueRequest queueRequest) throws Exception {
        userService.getUserPoint(queueRequest.getUserId()); // 존재하는 유저인지 검증하는 서비스 호출
        return queueService.registerQueue(queueRequest); //유저를 대기열에 등록하는 서비스
    }
}
