package com.example.concert.domain.concert.service;

import com.example.concert.domain.concert.entity.Concert;

public interface ConcertRepository {
   //도메인을 위한 인터페이스 선언
    Concert saveConcert(Concert concert);

    Concert getConcert(Long concertId);



    void deleteConcert(Long concertId);
}
