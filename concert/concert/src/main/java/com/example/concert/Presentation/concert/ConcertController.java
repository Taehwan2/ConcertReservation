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
    //user&point 기능을 포함한 userQueueFacade 추가
     private final UserQueueFacade userQueueFacade;

    //현재 대기열의 상태를 가져오는 api
    @GetMapping("/concert/wait/{userId}/{waitId}")
    public QueueResponse getQueue(@PathVariable(name = "userId")Long userId, @PathVariable(name = "waitId") Long waitId ) throws Exception {
                var queue  =  userQueueFacade.getQueue(userId,waitId);
                return Queue.entityToResponse(queue);

    }

    //queuereqeust 를 통한 대기열 등록 api
    @PostMapping("/concert/wait")
    public QueueResponse enrollQueue(@RequestBody QueueRequest queueRequest) throws Exception {
        var queue  =  userQueueFacade.enrollQueue(queueRequest);
        return Queue.entityToResponse(queue);

    }

}
