package com.example.concert.domain.queue.service;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class QueueService {
    private final int QUEUE_LIMIT_SIZE = 3;
    private final int QUEUE_EXPIRED_TIME = 1;

    private final QueueRepository queueRepository;

    public Queue getQueue(Long userId) throws Exception {
        Queue queue = queueRepository.findQueue(userId);
        queue.alreadyWorking();
        updateQueueRank(queue,queue.getQueueId());
        return queue;
    }


    private void updateQueueRank(Queue queue, Long waitId) {
        int ranking = queueRepository.findRanking(waitId,UserStatus.WORKING);
        queue.updateWaitingNumber(ranking);
    }
    @Transactional
    public Queue registerQueue(QueueRequest queueRequest) throws Exception {
        Long userId = queueRequest.getUserId();
        validateIfNotRegistered(userId);

        Queue queue = Queue.builder().userId(userId).build();
        validateAndRegisterWorking(queue);

        return queueRepository.saveQueue(queue);
    }

    @Scheduled(cron = "0 * * * * *")
    @Transactional
     public void  checkExpiredAtAndUpdateDone(){
        List<Queue> expiredList = queueRepository.findExpiredQueueOnWorking();

        if (expiredList == null || expiredList.isEmpty()) {
            return;
        }
        expiredQueues(expiredList);
        //Todo 바꿔야댐
        activeWaitingToWorking(expiredList.size());
     }

     @Transactional
    public void activeWaitingToWorking(int size) {
        if(size>QUEUE_LIMIT_SIZE)size=QUEUE_LIMIT_SIZE;
        List<Queue> waitingQueues = queueRepository.findWaitingQueues(size);
        waitingQueues.forEach(queue -> {
            queue.setWorking(QUEUE_EXPIRED_TIME);
        });
         queueRepository.saveAll(waitingQueues);
    }

    private void expiredQueues(List<Queue> expiredList) {
        expiredList.forEach(Queue::expiry);
         queueRepository.saveAll(expiredList);
    }

    private void validateAndRegisterWorking(Queue queue) {
        List<Queue> InQueueList = queueRepository.findStatusIsWorkingWithPessimisticLock(UserStatus.WORKING);
        if(InQueueList.size()<QUEUE_LIMIT_SIZE){
            queue.setWorking(QUEUE_EXPIRED_TIME);
        }else{
            queue.setWait();
        }
    }

    private void validateIfNotRegistered(Long userId) throws Exception {
        Queue queue = queueRepository.findByUserIdAndStatusIn(userId,List.of(UserStatus.WAITING,UserStatus.WORKING));
        if(queue==null)return;
        queue.alreadyWait();
        queue.alreadyWorking();;

    }

    public boolean isWorking(Long id){
       var isWorking = queueRepository.findWorkingQueue(id,UserStatus.WORKING);
        return isWorking;
    }



}
