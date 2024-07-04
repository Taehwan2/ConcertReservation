package com.example.concert.Presentation.concert;


import com.example.concert.Application.UserQueueFacade;
import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@SpringBootTest
@AutoConfigureMockMvc
class ConcertControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserQueueFacade userQueueFacade;

    @Test
    @DisplayName("사용자가 자신의 대기 상태를 받아올 수 있는 로직 검증")
    public void testGetQueue() throws Exception {
        Queue queue = new Queue(1L, 1L, UserStatus.WAITING, LocalDateTime.now());

        //given
        given(userQueueFacade.getQueue(eq(1L), eq(1L))).willReturn(queue);

        //when & then
        //Todo 현재 상태와 대기열 상태를 반환해야함
        //TOdo mapper 에서 대기열 번호 계산하는 로직 추가해야함
        mockMvc.perform(get("/concert/wait/1/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userStatus").value("WAITING"));
    }

    @Test
    @DisplayName("사용자가 자신을 대기열에 포함시킬 수 있는 코드")
    public void testEnrollQueue() throws Exception {
        Queue queue = new Queue(1L, 1L, UserStatus.WAITING, LocalDateTime.now());
        QueueRequest queueRequest = new QueueRequest(1L, UserStatus.WAITING);

        //given
        given(userQueueFacade.enrollQueue(any(QueueRequest.class))).willReturn(queue);

        //when & then
        //Todo 대기열 반환하기.
        mockMvc.perform(post("/concert/wait")
                        .contentType(APPLICATION_JSON)
                        .content("{\"userId\": 1, \"userStatus\": \"WAITING\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.queueId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.userStatus").value("WAITING"));
    }

}