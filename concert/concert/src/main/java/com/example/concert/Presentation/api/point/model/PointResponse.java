package com.example.concert.Presentation.point.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "point 조회 반환")
public class PointResponse {
    @Schema(description = "조회하는 사용자 아이디")
    private Long userId;

    @Schema(description = "조회하는 포인트")
    private BigDecimal point;
}
