package com.example.concert.Presentation.point.model;

import com.example.concert.domain.pointHistory.PointType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointHistoryResponse {
    private Long historyId;

    private Long userId;

    private PointType pointType;
}
