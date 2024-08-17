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
//콘서트 조회 api
public class ConcertAPIController {

    private final ConcertService concertService;
    //콘서트 조회
    //캐시저장을 위한 api
    @GetMapping("/{id}")
    public Concert getQueue(@PathVariable(name = "id") Long concertId)  {
        var concert  =  concertService.getConcert(concertId);
        return concert;

    }

    //콘서트 업데이트
    //캐시 업데이트를 위한 api
    @PatchMapping("/{id}")
    public void updateQueue(@PathVariable(name = "id") Long concertId ,@RequestBody ConcertReq concertReq)  {
     concertService.updateConcert(concertId,concertReq);

    }
    //콘서트 삭제
    //캐시삭제를 위한 api
    @DeleteMapping("/{id}")
    public void deleteQueue(@PathVariable(name = "id") Long concertId)  {
         concertService.deleteConcert(concertId);
    }
}
