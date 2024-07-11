package com.example.concert.domain.concertSeat.entity;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatResponse;
import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "concert_seat",
        uniqueConstraints = {
                @UniqueConstraint(name = "concertOptionId_seatNo",
                        columnNames = {"concert_detail_id", "seat_no"}
                )
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertSeatId;
    private  Long concertDetailId;
    private Long userId;
    @Enumerated(EnumType.STRING)
    private SeatStatus seatStatus;
    private Integer seatNo;
    private BigDecimal price;


    public static ConcertSeatResponse entityToResponse(ConcertSeat concertSeat){
        return ConcertSeatResponse.builder()
                .concertSeatId(concertSeat.getConcertSeatId())
                .concertDetailId(concertSeat.getConcertDetailId())
                .userId(concertSeat.getUserId())
                .seatStatus(concertSeat.getSeatStatus())
                .seatNo(concertSeat.getSeatNo())
                .price(concertSeat.getPrice())
                .build();

    }
}
