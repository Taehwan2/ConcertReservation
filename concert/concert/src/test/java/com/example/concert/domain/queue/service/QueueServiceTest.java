package com.example.concert.domain.queue.service;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;



    @Test
    @DisplayName("queue의 메인기능인 대기열을 가져오고 현재의 대기열 번호를 가져와서 보여주는 태스트.")
    void getQueueTest() throws Exception {
        Queue queue = Queue.builder().queueId(1L).userId(1L).userStatus(UserStatus.WAITING).waitingNumber(10).build();

        //given.
        given(queueRepository.findQueue(1L)).willReturn(queue);
        given(queueRepository.findRanking(1L,UserStatus.WORKING)).willReturn(5);

        //when.
        var result = queueService.getQueue(1L);

        assertThat(result.getWaitingNumber()).isEqualTo(5);

    }

    @Test
    @DisplayName("queue 를 등록하는데 이미 working인지 wait인지 확인하고, working할수있으면 working으로 등록하는 코드.")
    void registerQueueTest() throws Exception {
        Queue queue1 = Queue.builder().queueId(1L).userId(1L).userStatus(UserStatus.WORKING).waitingNumber(10).build();
        Queue queue2  = Queue.builder().queueId(2L).userId(1L).userStatus(UserStatus.WORKING).waitingNumber(10).build();
        Queue queue = Queue.builder().userId(1L).build();
       //given
        given(queueRepository.findByUserIdAndStatusIn(1L,List.of(UserStatus.WAITING,UserStatus.WAITING))).willReturn(null);

        given(queueRepository.findStatusIsWorkingWithPessimisticLock(UserStatus.WORKING)).willReturn(List.of(queue1,queue2));

        given(queueRepository.saveQueue(any(Queue.class))).willReturn(queue);
        //when
        var result = queueService.registerQueue(new QueueRequest(1L));
        //then
        assertThat(result.getExpiredAt()).isEqualTo(1);
    }
}