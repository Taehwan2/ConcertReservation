package com.example.concert.infrastructure.concert;

import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concert.service.ConcertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Repository
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    @Override
    public Concert saveConcert(Concert concert) {
        return concertJpaRepository.save(concert);
    }

    @Override
    public Concert getConcert(Long concertId) {
        return concertJpaRepository.findById(concertId).orElseThrow(()->new NoSuchElementException("NoSuchConcert"));
    }
}
