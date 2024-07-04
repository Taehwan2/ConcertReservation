package com.example.concert.Application;

import com.example.concert.domain.concertdetail.ConcertDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConcertFacade {
    public List<ConcertDetail> getAbleDates(Long concertDetailId) {
        return List.of();
    }
}
