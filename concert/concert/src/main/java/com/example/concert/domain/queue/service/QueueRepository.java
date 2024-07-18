package com.example.concert.domain.queue.service;

import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;

import java.util.List;


public interface QueueRepository {
     //도메인를 보호하기 위해서 둔 레파지토리 인터페이스
    //대기열 검색
    Queue findByUserIdAndStatusIn(Long userId, List<UserStatus> waiting);
    //대기열 갱신/저장
    Queue saveQueue(Queue queue);
    //대기열 검색
    List<Queue> findStatusIsWorkingWithPessimisticLock(UserStatus working);
    //대기열 검색
    Boolean findWorkingQueue(Long userId,UserStatus userStatus);
    //대기열 검색
    Queue findQueue(Long userId);
    //대기열 순서 조회
    int findRanking(Long waitId,UserStatus userStatus);
    //대기열 검색
    List<Queue> findExpiredQueueOnWorking();
    //전부 갱신 및 저장
    List<Queue> saveAll(List<Queue> expiredList);
    //대기열 검색
    List<Queue> findWaitingQueues(int size);
}
