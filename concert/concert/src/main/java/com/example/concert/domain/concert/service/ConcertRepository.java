package com.example.concert.domain.concert.service;

import com.example.concert.domain.concert.entity.Concert;

public interface ConcertRepository {

    Concert saveConcert(Concert concert);

    Concert getConcert(Long concertId);
}
