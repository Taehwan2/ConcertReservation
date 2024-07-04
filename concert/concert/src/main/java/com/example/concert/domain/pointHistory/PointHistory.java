package com.example.concert.domain.pointHistory;

import com.example.concert.Presentation.point.model.PointHistoryResponse;
import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public static PointHistoryResponse entityToResponse(PointHistory point) {
        return PointHistoryResponse.builder()
                .historyId(point.historyId)
                .userId(point.userId)
                .pointType(point.pointType)
                .build();
    }
}
