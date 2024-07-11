package com.example.concert.domain.concert.service;

import com.example.concert.domain.concert.entity.Concert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;

    public Concert saveConcert(Concert concert){
        return concertRepository.saveConcert(concert);
    }

    public Concert getConcert(Long concertId){
        return concertRepository.getConcert(concertId);
    }
}
