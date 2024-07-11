package com.example.concert.domain.concertdetail.service;

import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertDetailService {
    private final ConcertDetailRepository concertDetailRepository;

    public ConcertDetail saveConcertDetail(ConcertDetail concertDetail){
        return concertDetailRepository.saveConcertDetail(concertDetail);
    }

    public ConcertDetail getConcertDetail(Long concertDetailId){
        return concertDetailRepository.getConcert(concertDetailId);
    }


    public List<ConcertDetail> getAbleDates(Long concertId) {
        return concertDetailRepository.getAbleDates(concertId);
    }
}
