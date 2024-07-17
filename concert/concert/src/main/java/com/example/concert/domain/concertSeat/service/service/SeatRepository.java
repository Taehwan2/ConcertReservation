package com.example.concert.domain.concertSeat.service.service;

import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatRepository {
    List<ConcertSeat> findStatusReserved(Long concertDetailId, SeatStatus reserved);

    ConcertSeat findSeat(Long concertDetailId, int seatNo);

    ConcertSeat createSeat(ConcertSeat concertSeat);

    List<ConcertSeat> findExpiredInTemp(SeatStatus temp, LocalDateTime localDateTime);

    void changeSeatsStatus(List<Long> concertId);

    List<ConcertSeat> findTempSeatByUserId(Long userId);

    void updatedSeatToReserved(Long userId, List<Long> seatIds);
}
