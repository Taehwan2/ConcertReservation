package com.example.concert.domain.user.entity;

import com.example.concert.Presentation.point.model.PointResponse;


import com.example.concert.common.BaseEntity;
import com.example.concert.domain.user.pointHistory.enumType.PointType;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
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

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public static PointResponse entityToResponse(User point) {
        return PointResponse.builder()
                .userId(point.getUserId())
                .point(point.getPoint())
                .build();
    }

    public void calculate(BigDecimal amount) throws Exception {
        //변수 초기화
        BigDecimal tempAmount = this.getPoint().add(amount);

        // amount 0일시에 예외 처리
        if (amount.compareTo(BigDecimal.ZERO) == 0) throw new BusinessBaseException(ErrorCode.USER_AMOUNT_CANNOT_BE_ZERO);

        // 잔고가 0 이하일 때 예외 처리
        if (tempAmount.compareTo(BigDecimal.ZERO) < 0) throw  new BusinessBaseException(ErrorCode.USER_AMOUNT_CANNOT_BE_ZERO);


        this.setPoint(tempAmount);
    }
}
