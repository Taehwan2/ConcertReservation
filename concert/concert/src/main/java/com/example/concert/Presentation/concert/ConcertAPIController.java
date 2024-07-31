package com.example.concert.Presentation.concert;

import com.example.concert.Presentation.concert.model.concert.ConcertReq;
import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concert.service.ConcertService;
import com.example.concert.domain.queue.entitiy.Queue;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concerts")
public class ConcertAPIController {

    private final ConcertService concertService;

    @GetMapping("/{id}")
    public Concert getQueue(@PathVariable(name = "id") Long concertId)  {
        var concert  =  concertService.getConcert(concertId);
        return concert;

    }
    @PatchMapping("/{id}")
    public void updateQueue(@PathVariable(name = "id") Long concertId ,@RequestBody ConcertReq concertReq)  {
     concertService.updateConcert(concertId,concertReq);

    }

    @DeleteMapping("/{id}")
    public void deleteQueue(@PathVariable(name = "id") Long concertId)  {
         concertService.deleteConcert(concertId);
    }
}
