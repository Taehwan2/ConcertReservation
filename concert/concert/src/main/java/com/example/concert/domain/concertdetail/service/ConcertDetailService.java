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
   //콘서트 옵션을 저장하는 코드
    public ConcertDetail saveConcertDetail(ConcertDetail concertDetail){
        return concertDetailRepository.saveConcertDetail(concertDetail);
    }
   //콘서트 옵션을 들고오는 서비스
    public ConcertDetail getConcertDetail(Long concertDetailId){
        return concertDetailRepository.getConcert(concertDetailId);
    }
    //콘서트에서 예약가능한 날짜를 가져오는 콘서트 옵션의 서비스
    public List<ConcertDetail> getAbleDates(Long concertId) {
        return concertDetailRepository.getAbleDates(concertId);
    }
}
