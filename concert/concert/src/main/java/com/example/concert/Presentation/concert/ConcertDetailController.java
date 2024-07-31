
package com.example.concert.Presentation.concert;

import com.example.concert.Presentation.concert.model.concert.ConcertDetailReq;
import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/concert")
public class ConcertDetailController {
    private final ConcertDetailService concertDetailService;

    @GetMapping("/{id}")
    public ConcertDetail getQueue(@PathVariable(name = "id") Long concertDetailId)  {
        var concert  =  concertDetailService.getConcertDetail(concertDetailId);
        return concert;

    }
    @PatchMapping("/{id}")
    public void updateQueue(@PathVariable(name = "id") Long concertDetailId ,@RequestBody ConcertDetailReq concertDetailReq)  {
        concertDetailService.updateConcert(concertDetailId,concertDetailReq);

    }

    @DeleteMapping("/{id}")
    public void deleteQueue(@PathVariable(name = "id") Long concertDetailId)  {
        concertDetailService.deleteConcertDetail(concertDetailId);
    }

}

