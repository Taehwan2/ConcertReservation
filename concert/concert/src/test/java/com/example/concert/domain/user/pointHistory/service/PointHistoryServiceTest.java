package com.example.concert.domain.user.pointHistory.service;

import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import com.example.concert.domain.user.pointHistory.enumType.PointType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PointHistoryServiceTest {
    @InjectMocks
    private PointHistoryService pointHistoryService;

    @Mock
    private PointHistoryRepository pointHistoryRepository;


    @Test
    @DisplayName("간단한 PointHistorySave기능 테스트")
    void saveTest() {
        var history = PointHistory.builder()
                .userId(1L)
                .historyId(1L)
                .pointType(PointType.CHARGE)
                .amount(new BigDecimal(1000))
                .build();


        //given
        given(pointHistoryRepository.savePointHistory(history)).willReturn(history);

        //when
        var resultHistory = pointHistoryService.save(history);

        //then
        assertThat(history.getPointType()).isEqualTo(PointType.CHARGE);
        assertThat(history.getAmount()).isEqualTo(resultHistory.getAmount());

    }
}