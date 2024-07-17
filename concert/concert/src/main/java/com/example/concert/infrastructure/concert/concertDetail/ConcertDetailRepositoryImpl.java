package com.example.concert.infrastructure.concert.concertDetail;

import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailRepository;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
@RequiredArgsConstructor
public class ConcertDetailRepositoryImpl implements ConcertDetailRepository {
    private final ConcertDetailJpaRepository concertDetailJpaRepository;

    @Override
    public ConcertDetail saveConcertDetail(ConcertDetail concertDetail) {
        return concertDetailJpaRepository.save(concertDetail);
    }

    @Override
    public ConcertDetail getConcert(Long concertDetailId) {
        return concertDetailJpaRepository.findById(concertDetailId).orElseThrow(()->new BusinessBaseException(ErrorCode.CONCERT_DETAIL_NOT_FOUND));
    }

    @Override
    public List<ConcertDetail> getAbleDates(Long concertId) {
        return concertDetailJpaRepository.findAllByConcertIdAndBeforeStartDateAndReservationStartDate(concertId, LocalDateTime.now());
    }
}
