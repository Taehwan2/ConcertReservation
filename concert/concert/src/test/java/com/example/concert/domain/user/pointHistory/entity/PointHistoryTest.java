package com.example.concert.domain.user.pointHistory.entity;

import com.example.concert.domain.user.pointHistory.enumType.PointType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTest {
// 도메인 메서드 검증 unitTest
    @Test
    @DisplayName("도메인 로직 테스트 만약 Amount 에 따라서 올바를 타입을 반환하는 지 테스트.")
    void setPointTypeTest() {
        PointHistory pointHistory = new PointHistory(); //given
        pointHistory.checkType(new BigDecimal("-10"));  //when
        assertEquals(PointType.USE, pointHistory.getPointType());  //then
    }
    @DisplayName("양수를 넣었을 때 타입을 충전으로 반환하는 코드")
    @Test
    void setPointType() {
        PointHistory pointHistory = new PointHistory();  //given
        pointHistory.checkType(new BigDecimal(10));  //when
        assertEquals(PointType.CHARGE, pointHistory.getPointType()); //then
    }
    @DisplayName("0일때는 아무것도 반환하지 않는 코드")
    @Test
    public void testCheckTypeWithZeroAmount() {
        PointHistory pointHistory = new PointHistory(); //given
        pointHistory.checkType(BigDecimal.ZERO);  //when
        assertEquals(null, pointHistory.getPointType()); //then
    }


}