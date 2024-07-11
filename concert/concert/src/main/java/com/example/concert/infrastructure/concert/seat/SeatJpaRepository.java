package com.example.concert.infrastructure.concert.seat;

import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SeatJpaRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findByConcertDetailIdAndSeatStatusNot(Long concertDetailId, SeatStatus status);

    ConcertSeat findByUserIdAndConcertDetailId(Long userId, Long seatId);

    @Query("SELECT a FROM ConcertSeat a WHERE a.updatedAt <= :localDateTime and a.seatStatus =:temp")
    List<ConcertSeat> findByUpdatedAtAndStatus(SeatStatus temp, LocalDateTime localDateTime);

    @Modifying
    @Query("UPDATE ConcertSeat c set c.seatStatus = :status , c.userId = :userId WHERE c.concertSeatId IN :ids")
    void updateSeatStatusAndUserId(@Param("status")SeatStatus status, @Param("userId")Long userId, @Param("ids")List<Long> ids);

    List<ConcertSeat> findByUserIdAndSeatStatus(Long userId, SeatStatus temp);


}
