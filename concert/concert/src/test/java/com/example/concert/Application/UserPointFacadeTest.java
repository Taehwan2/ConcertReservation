package com.example.concert.Application;

import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import com.example.concert.domain.user.pointHistory.enumType.PointType;
import com.example.concert.domain.user.pointHistory.service.PointHistoryService;
import com.example.concert.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserPointFacadeTest {
    //유저 포인트 파사드 테스
    @InjectMocks
    private UserPointFacade userPointFacade;

    @Mock
    private UserService userService;

    @Mock
    private PointHistoryService pointHistoryService;

    @Test
    @DisplayName("간단한 기능 검증")
    void getUserPointTest() {
        var user = new User(1L,"태환",new BigDecimal(10000));
        //given
        given(userService.getUserPoint(1L)).willReturn(user);

        //when
       var userResult =  userPointFacade.getUserPoint(1L);

       //then
        assertThat(userResult.getName()).isEqualTo("태환");

    }

    @Test
    @DisplayName("포인트 조회 및 충전 파사드 검증")
    //유저 객층에 못넣어서 여기 추가
    void changePointTest() throws Exception {
        var pointRequest = new PointRequest(1L,new BigDecimal(1000));
        var user = new User(1L,"태환",new BigDecimal(2000));

        var history = PointHistory.builder()
                .userId(1L)
                .historyId(1L)
                .pointType(PointType.CHARGE)
                .amount(new BigDecimal(2000))
                .build();

        given(pointHistoryService.save(any(PointHistory.class))).willReturn(history);
        given(userPointFacade.getUserPoint(1L)).willReturn(user);

        var resultHistory = userPointFacade.changePoint(pointRequest);

        assertThat(resultHistory.getAmount()).isEqualTo(new BigDecimal(2000));
        assertThat(resultHistory.getUserId()).isEqualTo(1L);
    }
}