package com.example.concert.domain.point.entity;

import com.example.concert.Presentation.point.model.PointResponse;


import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "user")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String name;

    private BigDecimal point;


    public static PointResponse entityToResponse(User point) {
        return PointResponse.builder()
                .userId(point.getUserId())
                .point(point.getPoint())
                .build();
    }
}
