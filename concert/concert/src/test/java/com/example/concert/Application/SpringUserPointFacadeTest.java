package com.example.concert.Application;

import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.pointHistory.enumType.PointType;
import com.example.concert.domain.user.service.UserService;
import com.example.concert.infrastructure.user.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class SpringUserPointFacadeTest {

    @Autowired
    private UserPointFacade userPointFacade;

    @Autowired
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    public void saveUser(){
        var user = new User(1L,"taehwan",new BigDecimal(10000));
        userJpaRepository.save(user);
    }

    @Test
    void getUserPointTest() {

        var result = userPointFacade.getUserPoint(1L);
        assertThat(result.getPoint()).isEqualByComparingTo(new BigDecimal(10000));
    }

    @Test
    void changePointTest() throws Exception {
        var result = userPointFacade.changePoint(new PointRequest(1L,new BigDecimal(-1000000)));
        assertThat(result.getAmount()).isEqualByComparingTo(new BigDecimal(10000));
        assertThat(result.getPointType()).isEqualByComparingTo(PointType.CHARGE);
        var updatedUser = userJpaRepository.findById(1L).orElseThrow();
        assertThat(updatedUser.getPoint()).isEqualByComparingTo(new BigDecimal("11000"));
    }
}