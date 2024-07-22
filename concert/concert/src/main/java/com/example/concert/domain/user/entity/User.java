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
//사용자 객체
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
    //사용자 이름
    private String name;
   //사용자 포인트
    private BigDecimal point;
    //포인트 변환하는 로직
    public void setPoint(BigDecimal point) {
        this.point = point;
    }
    //엔티티를 DTO 로 변환하는 로직
    public static PointResponse entityToResponse(User point) {
        return PointResponse.builder()
                .userId(point.getUserId())
                .point(point.getPoint())
                .build();
    }
   //실제 계산하는 로직
    public void calculate(BigDecimal amount) throws Exception {
        //변수 초기화
        BigDecimal tempAmount = this.getPoint().add(amount);

        // amount 0일시에 예외 처리 에러코드로 담아서 비지니스 이셉션으로 잡는 로직
        if (amount.compareTo(BigDecimal.ZERO) == 0) throw new BusinessBaseException(ErrorCode.USER_AMOUNT_CANNOT_BE_ZERO);

        // 잔고가 0 이하일 때 예외 처리예 비지니스 이셉션으로 잡는 로직
        if (tempAmount.compareTo(BigDecimal.ZERO) < 0) throw  new BusinessBaseException(ErrorCode.USER_AMOUNT_CANNOT_BE_ZERO);

        //변환된 포인트 저장
        this.setPoint(tempAmount);
    }
}
