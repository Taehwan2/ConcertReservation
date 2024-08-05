
package com.example.concert.Presentation.concert;

import com.example.concert.Presentation.concert.model.concert.ConcertDetailReq;
import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
//콘서트 옵션 조회 api
@RequestMapping("/concert")
public class ConcertDetailController {
    private final ConcertDetailService concertDetailService;

    //콘서트 옵션 조회
    //캐시저장을 위한 api
    @GetMapping("/{id}")
    public ConcertDetail getQueue(@PathVariable(name = "id") Long concertDetailId)  {
        var concert  =  concertDetailService.getConcertDetail(concertDetailId);
        return concert;

    }

    //콘서트 옵션 업데이트
    //캐시 업데이트를 위한 api
    @PatchMapping("/{id}")
    public void updateQueue(@PathVariable(name = "id") Long concertDetailId ,@RequestBody ConcertDetailReq concertDetailReq)  {
        concertDetailService.updateConcert(concertDetailId,concertDetailReq);

    }

    //콘서트 옵션 삭제
    //캐시삭제를 위한 api
    @DeleteMapping("/{id}")
    public void deleteQueue(@PathVariable(name = "id") Long concertDetailId)  {
        concertDetailService.deleteConcertDetail(concertDetailId);
    }

}

