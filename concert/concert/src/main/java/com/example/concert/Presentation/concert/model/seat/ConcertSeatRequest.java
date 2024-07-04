package com.example.concert.Presentation.concert.model.seat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConcertSeatRequest {
    Long userId;
    Long seatId;
}
