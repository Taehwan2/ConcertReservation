package com.example.concert.Presentation.concert.model.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponse {
    private String concertName;
    private String concertGenre;
    private LocalDateTime startDate;
    private String seatNo;
    private String userName;
    private String price;
}
