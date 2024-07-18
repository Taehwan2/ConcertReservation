package com.example.concert.domain.concert.service;

import com.example.concert.domain.concert.entity.Concert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;
    //콘서트 저장 로직
    public Concert saveConcert(Concert concert){
        return concertRepository.saveConcert(concert);
    }
   //콘서트 가져오는 로직
    public Concert getConcert(Long concertId){
        return concertRepository.getConcert(concertId);
    }
}
