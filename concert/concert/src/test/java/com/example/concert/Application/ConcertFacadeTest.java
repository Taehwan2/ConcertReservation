package com.example.concert.Application;

import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
//콘서트 파사드 유닛 테스트
@ExtendWith(MockitoExtension.class)
class ConcertFacadeTest {

    @InjectMocks
    private ConcertFacade concertFacade;

    @Mock
    private ConcertDetailService concertDetailService;
   //ConcertFacade 의 기능을 검증하기 위해서 예약 가능한 일들을 미리 목 객체로 만들어놓고 기능 구현 검증
    @Test
    void getAbleDatesTest() {
        var concertDetail1 = new ConcertDetail(1L,1L, LocalDateTime.now(),LocalDateTime.now());
        var concertDetail2 = new ConcertDetail(2L,1L, LocalDateTime.now(),LocalDateTime.now());
        List<ConcertDetail> list = List.of(concertDetail1,concertDetail2);

        //given
        given(concertDetailService.getAbleDates(1L)).willReturn(list);

        //when
        var resultConcert = concertFacade.getAbleDates(1L);

        assertThat(resultConcert.get(0).getConcertId()).isEqualTo(1L);
        assertThat(resultConcert.get(1).getConcertId()).isEqualTo(1L);

    }
}