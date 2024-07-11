package com.example.concert.domain.user.pointHistory.entity;

import com.example.concert.domain.user.pointHistory.enumType.PointType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PointHistoryTest {

    @Test
    @DisplayName("도메인 로직 테스트 만약 Amount 에 따라서 올바를 타입을 반환하는 지 테스트")
    void setPointTypeTest() {
        PointHistory pointHistory = new PointHistory();
        pointHistory.checkType(new BigDecimal("-10"));
        assertEquals(PointType.USE, pointHistory.getPointType());
    }

    @Test
    void setPointType() {
        PointHistory pointHistory = new PointHistory();
        pointHistory.checkType(new BigDecimal(10));
        assertEquals(PointType.CHARGE, pointHistory.getPointType());
    }

    @Test
    public void testCheckTypeWithZeroAmount() {
        PointHistory pointHistory = new PointHistory();
        pointHistory.checkType(BigDecimal.ZERO);
        assertEquals(null, pointHistory.getPointType());
    }


}