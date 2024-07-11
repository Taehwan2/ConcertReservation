package com.example.concert.domain.concertdetail.service;

import com.example.concert.Application.ConcertFacade;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ConcertDetailServiceTest {

    @Mock
    private ConcertDetailRepository concertDetailRepository;

    @InjectMocks
    private ConcertDetailService concertDetailService;

    @Test
    @DisplayName("ConcertDetail 을 저장하는 기능 검증")
    void saveConcertDetailTest() {

       var concertDetail = new ConcertDetail(1L,1L, LocalDateTime.now(),LocalDateTime.now());
        //given
        given(concertDetailRepository.saveConcertDetail(concertDetail)).willReturn(concertDetail);

        //when
        var resultConcertDetail =  concertDetailService.saveConcertDetail(concertDetail);

        //then
        assertThat(resultConcertDetail.getConcertDetailId()).isEqualTo(concertDetail.getConcertDetailId());

    }

    @Test
    void getConcertDetailTest() {
        var concertDetail = new ConcertDetail(1L,1L, LocalDateTime.now(),LocalDateTime.now());

        //given
        given(concertDetailRepository.getConcert(1L)).willReturn(concertDetail);

        //when
        var resultConcertDetail =  concertDetailService.getConcertDetail(1L);

        //then
        assertThat(resultConcertDetail.getConcertDetailId()).isEqualTo(concertDetail.getConcertDetailId());
    }

    @Test
    void getAbleDatesTest() {
        LocalDateTime tenTimesAgo = LocalDateTime.now().minus(10, ChronoUnit.MINUTES);
        var concertDetail1 = new ConcertDetail(1L,1L, tenTimesAgo,LocalDateTime.now());
        var concertDetail2 = new ConcertDetail(2L,1L, LocalDateTime.now(),LocalDateTime.now());
        List<ConcertDetail> list = List.of(concertDetail1,concertDetail2);

        //given
        given(concertDetailRepository.getAbleDates(1L)).willReturn(list);

        //whne
        var concertDetailList = concertDetailService.getAbleDates(1L);

        //then
        assertThat(concertDetailList.get(0).getConcertId()).isEqualTo(1L);
        assertThat(concertDetailList.get(1).getConcertId()).isEqualTo(1L);


    }
}