package com.example.concert.Presentation.concert.model.date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DatesResponse {
    private Long concertId;

    private LocalDateTime startDate;
}
