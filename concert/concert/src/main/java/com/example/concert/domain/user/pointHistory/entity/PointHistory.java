package com.example.concert.domain.user.pointHistory.entity;

import com.example.concert.Presentation.point.model.PointHistoryResponse;
import com.example.concert.common.BaseEntity;
import com.example.concert.domain.user.pointHistory.enumType.PointType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "point_history")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    private Long userId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PointType pointType;

    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public static PointHistoryResponse entityToResponse(PointHistory point) {
        return PointHistoryResponse.builder()
                .historyId(point.historyId)
                .userId(point.userId)
                .pointType(point.pointType)
                .build();
    }

    public void checkType(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            this.setPointType(PointType.USE);
        } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
           this.setPointType(PointType.CHARGE);
        }
    }
}
