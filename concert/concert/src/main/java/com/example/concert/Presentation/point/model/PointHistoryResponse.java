package com.example.concert.Presentation.point.model;

import com.example.concert.domain.user.pointHistory.enumType.PointType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "point history")
public class PointHistoryResponse {
    @Schema(description = "포인트 내역 아이디" )
    private Long historyId;
    @Schema(description = "포인트 내역의 사용자")
    private Long userId;
    @Schema(description = "포인트 충전/사용 타입")
    private PointType pointType;
}
