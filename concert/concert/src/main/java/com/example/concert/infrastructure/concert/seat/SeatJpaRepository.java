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
    //낙관적 락 사용 --> 하지만 Lock을 안걸어도 ConcertDetailId와 SeatId에 유니크 제약조건이 걸려있기때문에
    //어짜피 한명만 통과하면 되기때문에 비관적 락을 걸필요 없음 락 획득 실패시 끝나느 로직
    //하나의 트렌젝션이 예약에 성공한다면 중복 에러로 동시성을 이미 처리했음
    @Query("SELECT c FROM ConcertSeat c Where c.concertDetailId = :concertDetailId AND c.seatNo = :seatNo")
    ConcertSeat findByConcertDetailIdAndSeatNo(Long concertDetailId, int seatNo);

    //이미 사용자가 좌석을 예약했지만 구매하지않고 userId와 예약가능한 상태로 남았을 때 userId와 좌석 상태를 업데이트 해주는 메서
    @Modifying
    @Query("UPDATE ConcertSeat c set c.seatStatus = :reservable , c.userId = :userId WHERE c.concertSeatId =:seatNo")
    int updateSeatStatusAndUserId(Long userId, SeatStatus reservable, Integer seatNo);
}
