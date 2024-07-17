package com.example.concert.infrastructure.queue;

import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import com.example.concert.domain.queue.service.QueueRepository;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    @Override
    public Queue findByUserIdAndStatusIn(Long userId, List<UserStatus> waiting) {
        return queueJpaRepository.findByUserIdAndUserStatusIn(userId,waiting);
    }

    @Override
    public Queue saveQueue(Queue queue) {
        return queueJpaRepository.save(queue);
    }

    @Override
    public List<Queue> findStatusIsWorkingWithPessimisticLock(UserStatus working) {
        return queueJpaRepository.findStatusIsWorkingWithPessimisticLock(working);
    }

    @Override
    public Boolean findWorkingQueue(Long userId,UserStatus userStatus) {
       if(queueJpaRepository.findByUserIdAndUserStatus(userId,userStatus).isPresent())return true;
       else return false;
    }

    @Override
    public Queue findQueue(Long userId) {
        return queueJpaRepository.findByUserId(userId).orElseThrow(()->new BusinessBaseException(ErrorCode.QUEUE_NOT_FOUND));
    }

    @Override
    public int findRanking(Long waitId,UserStatus userStatus) {
        return queueJpaRepository.getRanking(waitId,userStatus);
    }

    @Override
    public List<Queue> findExpiredQueueOnWorking() {
        return queueJpaRepository.findExpiredQueueOnWorking(UserStatus.WORKING, LocalDateTime.now());
    }

    @Override
    public List<Queue> saveAll(List<Queue> expiredList) {
        return queueJpaRepository.saveAll(expiredList);
    }

    @Override
    public List<Queue> findWaitingQueues(int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return queueJpaRepository.findUserStatusWaitingLimitSize(UserStatus.WAITING,pageRequest).getContent();
    }
}
