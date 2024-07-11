package com.example.concert.domain.queue.service;

import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;

import java.util.List;


public interface QueueRepository {


    Queue findByUserIdAndStatusIn(Long userId, List<UserStatus> waiting);

    Queue saveQueue(Queue queue);

    List<Queue> findStatusIsWorkingWithPessimisticLock(UserStatus working);

    Boolean findWorkingQueue(Long userId,UserStatus userStatus);

    Queue findQueue(Long userId);

    int findRanking(Long waitId,UserStatus userStatus);

    List<Queue> findExpiredQueueOnWorking();

    List<Queue> saveAll(List<Queue> expiredList);

    List<Queue> findWaitingQueues(int size);
}
