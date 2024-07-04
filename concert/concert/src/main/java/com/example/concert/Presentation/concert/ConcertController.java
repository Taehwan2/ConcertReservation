package com.example.concert.Presentation.concert;

import com.example.concert.Application.UserQueueFacade;
import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import com.example.concert.domain.queue.entitiy.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class ConcertController {

     private final UserQueueFacade userQueueFacade;


    @GetMapping("/concert/wait/{userId}/{waitId}")
    public QueueResponse getQueue(@PathVariable(name = "userId")Long userId, @PathVariable(name = "waitId") Long waitId ){
                var queue  =  userQueueFacade.getQueue(userId,waitId);
                return Queue.entityToResponse(queue);

    }

    @PostMapping("/concert/wait")
    public QueueResponse enrollQueue(@RequestBody QueueRequest queueRequest){
        var queue  =  userQueueFacade.enrollQueue(queueRequest);
        return Queue.entityToResponse(queue);

    }

}
