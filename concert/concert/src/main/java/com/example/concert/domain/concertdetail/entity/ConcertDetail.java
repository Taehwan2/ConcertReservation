package com.example.concert.domain.concertdetail.entity;

import com.example.concert.Presentation.concert.model.date.DatesResponse;
import com.example.concert.common.BaseEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;


@Entity
//콘서트 예약 가능일을 처리하기 위해서 만든 인덱스
@Table(name = "concert_detail",
        indexes = {
                @Index(name = "idx_concert_id_start_date_reservation_start_date",
                   columnList = "concert_id, start_date, reservation_start_date") //추가
        })
@Getter
@Setter
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
