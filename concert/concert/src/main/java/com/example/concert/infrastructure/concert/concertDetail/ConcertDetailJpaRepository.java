package com.example.concert.infrastructure.concert.concertDetail;

import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertDetailJpaRepository extends JpaRepository<ConcertDetail,Long> {
    //콘서트 예약일과 콘서트 시작일이 전인 것만 가져오는 쿼리
    @Query("SELECT c FROM ConcertDetail c where  :now<c.startDate  and :now <c.reservationStartDate and c.concertId =:concertId")
    List<ConcertDetail> findAllByConcertIdAndBeforeStartDateAndReservationStartDate(@Param("concertId") Long concertId, @Param("now") LocalDateTime now);


}
