package com.example.concert.Application;

import com.example.concert.domain.concert.service.ConcertService;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertDetailService concertDetailService;
    private final ConcertService concertService;
  //콘서트를 먼저 조회하고 콘서트가 있다면 콘서트의 옵션을 통해서 예약가능일 검증
    @Transactional
    public List<ConcertDetail> getAbleDates(Long concertId) {
        concertService.getConcert(concertId);
        return concertDetailService.getAbleDates(concertId);
    }
}
