package com.example.concert.infrastructure.concert.seat;

import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import com.example.concert.domain.concertSeat.service.service.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Override
    public List<ConcertSeat> findStatusReserved(Long concertDetailId, SeatStatus reserved) {
        return seatJpaRepository.findByConcertDetailIdAndSeatStatus(concertDetailId,reserved);
    }

    @Override
    public ConcertSeat findSeat(Long userId, Long concertDetailId) {
        return seatJpaRepository.findByUserIdAndConcertDetailId(userId,concertDetailId);
    }

    @Override
    public ConcertSeat createSeat(ConcertSeat concertSeat) {
        return seatJpaRepository.save(concertSeat);
    }

    @Override
    public List<ConcertSeat> findExpiredInTemp(SeatStatus temp, LocalDateTime localDateTime) {
        return seatJpaRepository.findByUpdatedAtAndStatus(temp,localDateTime);
    }

    @Override
    public void changeSeatsStatus(List<Long> concertId) {
        seatJpaRepository.updateSeatStatusAndUserId(SeatStatus.RESERVABLE,null,concertId);
    }

    @Override
    public List<ConcertSeat> findTempSeatByUserId(Long userId) {
        return seatJpaRepository.findByUserIdAndSeatStatus(userId,SeatStatus.TEMP);
    }

    @Override
    public void updatedSeat(Long userId, List<Long> seatIds) {
        seatJpaRepository.updateSeatStatusAndUserId(SeatStatus.RESERVED,userId,seatIds);
    }
}
