package com.example.concert.domain.concertdetail.service;

import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;

import java.util.List;

public interface ConcertDetailRepository {
    ConcertDetail saveConcertDetail(ConcertDetail concertDetail);
   //도메인을 위한 레파지토리 인터페이스 선언
    ConcertDetail getConcert(Long concertDetailId);

    List<ConcertDetail> getAbleDates(Long concertId);

    void deleteConcertDetail(Long concertDetailId);
}
