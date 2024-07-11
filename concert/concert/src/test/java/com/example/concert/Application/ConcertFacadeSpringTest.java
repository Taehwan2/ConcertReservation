package com.example.concert.Application;

import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concert.service.ConcertService;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ConcertFacadeSpringTest {

    @Autowired
    private ConcertDetailService concertDetailService;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertFacade concertFacade;


    @BeforeEach
    public void before(){
        var concert = new Concert(1L,"KingsMan","action");
        var concertDetail = new ConcertDetail(1L,1L, LocalDateTime.now(),LocalDateTime.now());
        var concertDetail2 = new ConcertDetail(2L,1L,LocalDateTime.now(),LocalDateTime.now());
         concertService.saveConcert(concert);
         concertDetailService.saveConcertDetail(concertDetail);
         concertDetailService.saveConcertDetail(concertDetail2);
    }

    @Test
    @DisplayName("예약 가능날짜 가져오는 로직")
    public  void getAbleDatesTest(){

        var result = concertFacade.getAbleDates(1L);
        System.out.println("출력 결과:");
        for (var date : result) {
            System.out.println("Concert ID: " + date.getConcertId() + ", Start Time: " + date.getStartDate() + ", End Time: " + date.getReservationStartDate());
        }
        assertThat(result.get(0).getConcertId()).isEqualTo(1L);
        assertThat(result.get(1).getConcertId()).isEqualTo(1L);


    }


}