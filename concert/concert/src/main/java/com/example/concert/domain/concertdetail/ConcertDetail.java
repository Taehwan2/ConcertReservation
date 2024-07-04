package com.example.concert.domain.concertdetail;

import com.example.concert.Presentation.concert.model.date.DatesResponse;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatResponse;
import com.example.concert.common.BaseEntity;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
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

    private Long concertId;

    private LocalDateTime startDate;

    private LocalDateTime reservationStartDate;

    public static DatesResponse entityToResponse(ConcertDetail concertDetail){
        return DatesResponse.builder()
                .concertId(concertDetail.getConcertId())
                .startDate(concertDetail.getStartDate())
                .build();

    }
}
