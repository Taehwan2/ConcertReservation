package com.example.concert.infrastructure.concert.seat;

import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertSeat.entity.SeatStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<ConcertSeat, Long> {

    List<ConcertSeat> findByConcertDetailIdAndSeatStatusNot(Long concertDetailId, SeatStatus status);
   //사용자의 아이디와 지정된 좌석이있는지 가져온다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    ConcertSeat findByUserIdAndConcertDetailId(Long userId, Long seatId);
    //만료시간이 다 지난 좌석을 가져온다.
    @Query("SELECT a FROM ConcertSeat a WHERE a.updatedAt <= :localDateTime and a.seatStatus =:temp")
    List<ConcertSeat> findByUpdatedAtAndStatus(SeatStatus temp, LocalDateTime localDateTime);
    //조회된 좌석들을 들고와서 상태를 변경시켜준다.
    @Modifying
    @Query("UPDATE ConcertSeat c set c.seatStatus = :status , c.userId = :userId WHERE c.concertSeatId IN :ids")
    void updateSeatStatusAndUserId(@Param("status")SeatStatus status, @Param("userId")Long userId, @Param("ids")List<Long> ids);
  //예약되어있는 좌석들고오기
    List<ConcertSeat> findByUserIdAndSeatStatus(Long userId, SeatStatus temp);

   //유닉크키가 걸려있는 부분 들고오기
    ConcertSeat findByConcertDetailIdAndSeatNo(Long concertDetailId, int seatNo);
}
