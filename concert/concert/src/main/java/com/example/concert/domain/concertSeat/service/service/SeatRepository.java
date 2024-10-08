package com.example.concert.domain.concertSeat.service.service;

import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;
//도메인을 위해서 리파지토리 계층 생성
public interface SeatRepository {
    List<ConcertSeat> findStatusReserved(Long concertDetailId, SeatStatus reserved);

    ConcertSeat findSeat(Long concertDetailId, int seatNo);

    ConcertSeat createSeat(ConcertSeat concertSeat);

    List<ConcertSeat> findExpiredInTemp(SeatStatus temp, LocalDateTime localDateTime);

    void changeSeatsStatus(List<Long> concertId);

    List<ConcertSeat> findTempSeatByUserId(Long userId);

    void updatedSeatToReserved(Long userId, List<Long> seatIds);

    Integer updateSeat(ConcertSeat concertSeat);
}
