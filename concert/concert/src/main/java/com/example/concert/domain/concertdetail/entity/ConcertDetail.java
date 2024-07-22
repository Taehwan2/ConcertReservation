package com.example.concert.domain.concertdetail.entity;

import com.example.concert.Presentation.concert.model.date.DatesResponse;
import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "concert_detail")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertDetailId;
    //콘서트에 옵션에 대한 내용
    private Long concertId;
     //콘서트 시작일
    private LocalDateTime startDate;
    //콘서트 예약가능일
    private LocalDateTime reservationStartDate;
   //엔티티변환
    public static DatesResponse entityToResponse(ConcertDetail concertDetail){
        return DatesResponse.builder()
                .concertId(concertDetail.getConcertId())
                .startDate(concertDetail.getStartDate())
                .build();

    }
}
