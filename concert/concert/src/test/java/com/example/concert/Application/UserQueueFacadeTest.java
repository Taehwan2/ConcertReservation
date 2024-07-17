package com.example.concert.Application;

import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import com.example.concert.domain.queue.service.QueueService;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserQueueFacadeTest {
    @Mock
    private UserService userService;

    @Mock
    private QueueService queueService;

    @InjectMocks
    private UserQueueFacade userQueueFacade;

    @Test
    void getQueueTest() throws Exception {
        User user = User.builder().userId(1L).name("태환").build();
        Queue queue = Queue.builder().queueId(1L).userId(1L).userStatus(UserStatus.WAITING).waitingNumber(10).build();
        //given
        given(userService.getUserPoint(1L)).willReturn(user);
        given(queueService.getQueue(1L)).willReturn(queue);

        //when
        var result = userQueueFacade.getQueue(1L,1L);

        //then
        assertThat(result.getQueueId()).isEqualTo(result.getQueueId());
        assertThat(result.getUserStatus()).isEqualTo(result.getUserStatus());

    }

    @Test
    void enrollQueueTest() throws Exception {
        User user = User.builder().userId(1L).name("태환").build();
        Queue queue = Queue.builder().queueId(1L).userId(1L).userStatus(UserStatus.WAITING).waitingNumber(10).build();
        QueueRequest queueRequest = new QueueRequest(1L);
        //given
        given(userService.getUserPoint(1L)).willReturn(user);
        given(queueService.registerQueue(queueRequest)).willReturn(queue);

        var result = userQueueFacade.enrollQueue(queueRequest);

        //then
        assertThat(result.getQueueId()).isEqualTo(result.getQueueId());
        assertThat(result.getUserStatus()).isEqualTo(result.getUserStatus());
    }
}