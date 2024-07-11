package com.example.concert.domain.concertdetail.service;

import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;

import java.util.List;

public interface ConcertDetailRepository {
    ConcertDetail saveConcertDetail(ConcertDetail concertDetail);

    ConcertDetail getConcert(Long concertDetailId);

    List<ConcertDetail> getAbleDates(Long concertId);
}
