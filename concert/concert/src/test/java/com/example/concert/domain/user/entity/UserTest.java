package com.example.concert.domain.user.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//유저 도메인 메서드를 테스트하는 코드
class UserTest {

    @Test
    @DisplayName("도메인의  더하기 계산 로직이 맞는 지 검증하는 테스트.")
    void calculateTest() throws Exception {
        var user = new User(1L,"태환",new BigDecimal(10000));  //given
        user.calculate(new BigDecimal(20000));  //when
        assertThat(user.getPoint()).isEqualTo(new BigDecimal(30000)); //then

    }

    @Test
    @DisplayName("도메인의  빼기 계산 로직이 맞는 지 검증하는 테스트.")
    void calculateTest2() throws Exception {
        var user = new User(1L,"태환",new BigDecimal(20000)); //give
        user.calculate(new BigDecimal(-10000));
        assertThat(user.getPoint()).isEqualTo(new BigDecimal(20000));
    }

    @Test
    @DisplayName("도메인의  잔고가 0일 때 로직이 맞는 지 검증하는 테스트.")
    void calculateExceptionTest() throws Exception {
        var user = new User(1L,"태환",new BigDecimal(20000));

        Assertions.assertThrows(Exception.class, ()-> {
            user.calculate(new BigDecimal(-30000));
        }
        );

    }
    @Test
    @DisplayName("도메인의  amount 0일 때 로직이 맞는 지 검증하는 테스트.")
    void calculateExceptionTest2() throws Exception {
        var user = new User(1L,"태환",new BigDecimal(20000));

        Assertions.assertThrows(Exception.class, ()-> {
                    user.calculate(new BigDecimal(0));
                }
        );

    }
}