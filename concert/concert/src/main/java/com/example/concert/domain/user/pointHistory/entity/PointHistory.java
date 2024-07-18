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
    //결과를 저장할 유저의 아이디
    private Long userId;
   ///충전이나 사용한 금액의 양
    private BigDecimal amount;
    //사용 이나 충전
    @Enumerated(EnumType.STRING)
    private PointType pointType;
    //사용이나 충전을 수정하는 메서드
    public void setPointType(PointType pointType) {
        this.pointType = pointType;
    }
   //결과값에 사용이나 충전의 값을 넣어주는 메서드
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
  //엔티티 리스펀스로 변환
    public static PointHistoryResponse entityToResponse(PointHistory point) {
        return PointHistoryResponse.builder()
                .historyId(point.historyId)
                .userId(point.userId)
                .pointType(point.pointType)
                .build();
    }
   // PointHistory 결과를 저장할때 금액이 음수면 사용 양수면 충전으로 확인하는 메서드
    public void checkType(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            this.setPointType(PointType.USE);
        } else if (amount.compareTo(BigDecimal.ZERO) > 0) {
           this.setPointType(PointType.CHARGE);
        }
    }
}
