package com.example.concert.Presentation.point.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointRequest {
    private Long userId;
    private BigDecimal charge;
}
