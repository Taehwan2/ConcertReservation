package com.example.concert.domain.concertSeat.entity;

import com.example.concert.Presentation.concert.model.seat.ConcertSeatResponse;
import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "concert_seat")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long concertSeatId;
    private  Long concertDetailId;
    private SeatStatus seatStatus;
    private String seatNo;
    private BigDecimal price;


    public static ConcertSeatResponse entityToResponse(ConcertSeat concertSeat){
        return ConcertSeatResponse.builder()
                .concertSeatId(concertSeat.getConcertSeatId())
                .concertDetailId(concertSeat.getConcertDetailId())
                .seatStatus(concertSeat.getSeatStatus())
                .seatNo(concertSeat.getSeatNo())
                .price(concertSeat.getPrice())
                .build();

    }
}
