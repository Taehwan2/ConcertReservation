package com.example.concert.infrastructure.concert.seat;

import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import com.example.concert.domain.concertSeat.service.service.SeatRepository;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;
  //1
    @Override
    public List<ConcertSeat> findStatusReserved(Long concertDetailId, SeatStatus reserved) {
        return seatJpaRepository.findByConcertDetailIdAndSeatStatusNot(concertDetailId,reserved);
    }
    //좌석을 예약하기전에 좌석을 조회하는 로직 -> 비관적 락 걸어도 되고 안걸어도된다 -> 낙관적 Lock은 효율이없기에 아예 배제
    @Override
    public ConcertSeat findSeat(Long concertDetailId,int seatNo) {
        return seatJpaRepository.findByConcertDetailIdAndSeatNo(concertDetailId,seatNo);
    }
//3
    @Override
    public ConcertSeat createSeat(ConcertSeat concertSeat) {
        return seatJpaRepository.save(concertSeat);
    }
//4
    @Override
    public List<ConcertSeat> findExpiredInTemp(SeatStatus temp, LocalDateTime localDateTime) {
        return seatJpaRepository.findByUpdatedAtAndStatus(temp,localDateTime);
    }
//5
    @Override
    public void changeSeatsStatus(List<Long> concertId) {
        seatJpaRepository.updateSeatStatusAndUserId(SeatStatus.RESERVABLE,null,concertId);
    }
//6
    @Override
    public List<ConcertSeat> findTempSeatByUserId(Long userId) {
        return seatJpaRepository.findByUserIdAndSeatStatus(userId,SeatStatus.TEMP);
    }
//
    @Override
    public void updatedSeatToReserved(Long userId, List<Long> seatIds) {
        seatJpaRepository.updateSeatStatusAndUserId(SeatStatus.RESERVED,userId,seatIds);
    }

    //좌석을 업데이트하는 쿼리
    @Override
    public Integer updateSeat(ConcertSeat concertSeat) {
        return seatJpaRepository.updateSeatStatusAndUserId(concertSeat.getUserId(),SeatStatus.TEMP,concertSeat.getSeatNo());
    }
}
