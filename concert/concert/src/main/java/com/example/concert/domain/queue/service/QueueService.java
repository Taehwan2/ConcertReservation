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
    private final int QUEUE_LIMIT_SIZE = 3; //대기열에 들어올 수 있는 사용자의 크기
    private final int QUEUE_EXPIRED_TIME = 1; //실행시 대기열이 만료되는 시간

    private final QueueRepository queueRepository;

    public Queue getQueue(Long userId) throws Exception {
        Queue queue = queueRepository.findQueue(userId); //사용자의 아이디로 대기열을 조회
        queue.alreadyWorking(); // 실행중인지 확인하는 메서드
        updateQueueRank(queue,queue.getQueueId()); //대기순서를 확인하고 업데이트 해주는 메서드
        return queue; //대기열 반환
    }


    private void updateQueueRank(Queue queue, Long waitId) {
        int ranking = queueRepository.findRanking(waitId,UserStatus.WAITING);  //대기중인 사람들중 순위를 가져온다
        queue.updateWaitingNumber(ranking); //도메인 서비스를 통한 업데이트
    }
    @Transactional
    public Queue registerQueue(QueueRequest queueRequest) throws Exception {
        Long userId = queueRequest.getUserId(); //유저를 가져와서
        validateIfNotRegistered(userId); //유저의 대기열을 가져와서 검증하는 코드

        Queue queue = Queue.builder().userId(userId).build();
        validateAndRegisterWorking(queue); //만약 실행가능한 상태라면 실행으로 업데이트 해준다.

        return queueRepository.saveQueue(queue);
    }

    @Scheduled(cron = "0 * * * * *") //스케줄러 사용 추후에 스케줄러 페키지를 따로 두고 이동
    @Transactional
     public void  checkExpiredAtAndUpdateDone(){
        List<Queue> expiredList = queueRepository.findExpiredQueueOnWorking(); //매초당 만료해야되는 대기열 검색

        if (expiredList == null || expiredList.isEmpty()) {
            return;
        }
        expiredQueues(expiredList); //대기열이 비어있지않다면 대기열 만료처리
        //Todo 바꿔야댐
        activeWaitingToWorking(expiredList.size()); //남아있는 대기중인 상태들 실행으로 변경
     }

     @Transactional
    public void activeWaitingToWorking(int size) {
        if(size>QUEUE_LIMIT_SIZE)size=QUEUE_LIMIT_SIZE; //사이즈를 보고 그 사이즈만큼 실행으로 전환
        List<Queue> waitingQueues = queueRepository.findWaitingQueues(size);
        waitingQueues.forEach(queue -> {
            queue.setWorking(QUEUE_EXPIRED_TIME);
        });
         queueRepository.saveAll(waitingQueues);
    }

    private void expiredQueues(List<Queue> expiredList) {
        expiredList.forEach(Queue::expiry); //만료상태를 입력해주는 도메인 서비스를 실행하고 상태를 저장
         queueRepository.saveAll(expiredList);
    }

    private void validateAndRegisterWorking(Queue queue) {
        List<Queue> InQueueList = queueRepository.findStatusIsWorkingWithPessimisticLock(UserStatus.WORKING);
        if(InQueueList.size()<QUEUE_LIMIT_SIZE){ //사이즈에 들어갈 수 있으면 실행으로 아니면 기다림으로
            queue.setWorking(QUEUE_EXPIRED_TIME);
        }else{
            queue.setWait();
        }
    }

    private void validateIfNotRegistered(Long userId) throws Exception {
        Queue queue = queueRepository.findByUserIdAndStatusIn(userId,List.of(UserStatus.WAITING,UserStatus.WORKING));
        if(queue==null)return; //대기열이 없으면 성공
        queue.alreadyWait(); //대기중이거나 실행중이면 실패
        queue.alreadyWorking();;

    }

    public boolean isWorking(Long id){
       var isWorking = queueRepository.findWorkingQueue(id,UserStatus.WORKING);
        return isWorking;
    }



}
