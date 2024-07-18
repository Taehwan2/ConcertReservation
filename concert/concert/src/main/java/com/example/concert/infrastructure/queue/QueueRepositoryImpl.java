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
    //대기열 저장
    @Override
    public Queue saveQueue(Queue queue) {
        return queueJpaRepository.save(queue);
    }
   //실행중인 사용자 순차적으로 조회
    @Override
    public List<Queue> findStatusIsWorkingWithPessimisticLock(UserStatus working) {
        return queueJpaRepository.findStatusIsWorkingWithPessimisticLock(working);
    }
    //실행중인지 확인
    @Override
    public Boolean findWorkingQueue(Long userId,UserStatus userStatus) {
       if(queueJpaRepository.findByUserIdAndUserStatus(userId,userStatus).isPresent())return true;
       else return false;
    }
   //대기열 찾기
    @Override
    public Queue findQueue(Long userId) {
        return queueJpaRepository.findByUserId(userId).orElseThrow(()->new BusinessBaseException(ErrorCode.QUEUE_NOT_FOUND));
    }
   //순서찾기
    @Override //사용자가 대기열 몇번인지 확인하는 코드
    public int findRanking(Long waitId,UserStatus userStatus) {
        return queueJpaRepository.getRanking(waitId,userStatus);
    }
    //만료된 대기열 가져오기
    @Override
    public List<Queue> findExpiredQueueOnWorking() {
        return queueJpaRepository.findExpiredQueueOnWorking(UserStatus.WORKING, LocalDateTime.now());
    }
    //전체 저장 및 수정
    @Override
    public List<Queue> saveAll(List<Queue> expiredList) {
        return queueJpaRepository.saveAll(expiredList);
    }
    //대기중인 번호
    @Override
    public List<Queue> findWaitingQueues(int size) {
        PageRequest pageRequest = PageRequest.of(0, size);
        return queueJpaRepository.findUserStatusWaitingLimitSize(UserStatus.WAITING,pageRequest).getContent();
    }
}
