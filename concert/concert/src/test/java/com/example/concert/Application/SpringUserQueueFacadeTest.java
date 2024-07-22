package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.UserStatus;
import com.example.concert.domain.queue.service.QueueService;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringUserQueueFacadeTest {

    @Autowired
    private UserService userService;

    @Autowired
    private QueueService queueService;

    @Autowired
    private UserQueueFacade userQueueFacade;

  //실제 통합 테스트로 대기열을 검증하는 테스트 구현
    @BeforeEach
    // 실제 데이터 베이스에 기존에 필요한 셋팅
    public void enroll() throws Exception {
        var user = new User(1L,"taehwan",new BigDecimal(10000));
        var user1 = new User(2L,"taehwan",new BigDecimal(10000));
        var user2 = new User(3L,"taehwan",new BigDecimal(10000));
        var user3 = new User(4L,"taehwan",new BigDecimal(10000));
        var user4 = new User(5L,"taehwan",new BigDecimal(10000));
        var user5 = new User(6L,"taehwan",new BigDecimal(10000));
        var user6 = new User(7L,"taehwan",new BigDecimal(10000));
        var user7 = new User(8L,"taehwan",new BigDecimal(10000));
        userService.save(user);
        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);
        userService.save(user5);
        userService.save(user6);
        QueueRequest queueRequest = new QueueRequest(1L);
        QueueRequest queueRequest2 = new QueueRequest(2L);
        QueueRequest queueRequest3 = new QueueRequest(3L);
        QueueRequest queueRequest4 = new QueueRequest(4L);
        QueueRequest queueRequest5 = new QueueRequest(5L);
        QueueRequest queueRequest6 = new QueueRequest(6L);
        userQueueFacade.enrollQueue(queueRequest);
        userQueueFacade.enrollQueue(queueRequest2);
        userQueueFacade.enrollQueue(queueRequest3);
        userQueueFacade.enrollQueue(queueRequest4);
        userQueueFacade.enrollQueue(queueRequest5);
        userQueueFacade.enrollQueue(queueRequest6);

    }
    @Test
    @DisplayName("실제 대기열을 가져오는 테스트")
    void getQueue() throws Exception {
        var queue = userQueueFacade.getQueue(6L,6L);
        assertThat(queue.getWaitingNumber()).isEqualTo(3);
    }

    @Test
    @DisplayName("실제 대기열을 등록하는 테스트")
    void enrollQueue() throws Exception {
        QueueRequest queueRequest = new QueueRequest(7L);
        var queue =userQueueFacade.enrollQueue(queueRequest);
        assertThat(queue.getUserStatus()).isEqualTo(UserStatus.WAITING);

    }

  /*  @Test
    void expiredQueuesTest(){
        Queue queue1 = Queue.builder().queueId(1L).userId(1L).userStatus(UserStatus.WORKING).build();
        Queue queue2 = Queue.builder().queueId(2L).userId(2L).userStatus(UserStatus.WORKING).build();
        List<Queue> queueList = List.of(queue1,queue2);
        var result = queueService.expiredQueues(queueList);
        System.out.println(result.get(0).getUserStatus());
        System.out.println(result.get(1).getUserStatus());

    }*/
    /*
    @Test
    void expiredQueuesTest(){

        var result = queueService.activeWaitingToWorking(5);
        System.out.println(result.get(0).getUserStatus());
        System.out.println(result.get(1).getUserStatus());
        System.out.println(result.get(1).getExpiredAt());
        System.out.println(result.get(2).getExpiredAt());


    }

     */
}