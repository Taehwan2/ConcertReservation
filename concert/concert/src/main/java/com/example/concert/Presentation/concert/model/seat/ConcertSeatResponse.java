package com.example.concert.Presentation.concert.model.seat;

import com.example.concert.domain.concertSeat.entity.SeatStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class ConcertSeatResponse {
    private Long concertSeatId;
    private  Long concertDetailId;
    private Long userId;
    private SeatStatus seatStatus;
    private Integer seatNo;
    private BigDecimal price;
}
