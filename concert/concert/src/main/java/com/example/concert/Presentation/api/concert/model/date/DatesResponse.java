package com.example.concert.Presentation.concert.model.date;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "예약가능 일")
public class DatesResponse {
    @Schema(description ="콘서트아이디" )
    private Long concertId;

    @Schema(description = "예약가능일")
    private LocalDateTime startDate;
}
