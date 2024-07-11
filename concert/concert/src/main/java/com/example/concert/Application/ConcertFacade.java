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

    @Transactional
    public List<ConcertDetail> getAbleDates(Long concertId) {
        concertService.getConcert(concertId);
        return concertDetailService.getAbleDates(concertId);
    }
}
