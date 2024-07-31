package com.example.concert.infrastructure.concert;

import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concert.service.ConcertRepository;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ConcertRepositoryImpl implements ConcertRepository {
   //콘서트 레파지토리를 구현한 구현체
    private final ConcertJpaRepository concertJpaRepository;

    //콘서트 저장
    @Override
    public Concert saveConcert(Concert concert) {
        return concertJpaRepository.save(concert);
    }
   //콘서트 조회
    @Override
    public Concert getConcert(Long concertId) {
        return concertJpaRepository.findById(concertId).orElseThrow(()->new BusinessBaseException(ErrorCode.CONCERT_NOT_FOUND));
    }


    @Override
    public void deleteConcert(Long concertId) {
         concertJpaRepository.deleteById(concertId);
    }
}
