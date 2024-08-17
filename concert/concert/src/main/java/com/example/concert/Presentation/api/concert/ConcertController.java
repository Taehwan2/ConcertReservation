package com.example.concert.Presentation.concert;

import com.example.concert.Application.UserQueueFacade;
import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import com.example.concert.Presentation.concert.swaggerController.ConcertWaitingDocsController;
import com.example.concert.domain.queue.entitiy.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ConcertController implements ConcertWaitingDocsController {
     private final UserQueueFacade userQueueFacade;

    //현재 대기열의 번호를 가져오는 api.
    @GetMapping("/concert/wait/{userId}/{waitId}")
    public Long getQueue(@PathVariable(name = "userId")Long userId, @PathVariable(name = "waitId") Long waitId ) throws Exception {
        return  userQueueFacade.getQueue(userId,waitId);  //파사드 계층을 이용하여 대기열 조회
    }

    //queuereqeust 를 통한 대기열 등록 api.
    @PostMapping("/concert/wait")
    public boolean enrollQueue(@RequestBody QueueRequest queueRequest) throws Exception {
       return  userQueueFacade.enrollQueue(queueRequest);  //대기열 등록
    }

}
