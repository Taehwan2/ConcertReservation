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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//실제 ConcertFacade 통합 테스트
@SpringBootTest
class ConcertFacadeSpringTest {

    @Autowired
    private ConcertDetailService concertDetailService;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ConcertFacade concertFacade;

   //데이터 선처리 작업

    @BeforeEach
    public void before(){
        var concert = new Concert(1L,"KingsMan","action"); //콘서트 입력
        var concert2 = new Concert(2L,"KingsMan2","action"); //콘서트 입력
        var concert3 = new Concert(3L,"KingsMan3","action"); //콘서트 입력
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tenMinutesLater = now.plusMinutes(10);
        List<ConcertDetail> details = new ArrayList<>();
        concertService.saveConcert(concert); //콘서트 저장
        concertService.saveConcert(concert2); //콘서트 저장
        concertService.saveConcert(concert3); //콘서트 저장
        for(int i=0;i<5000;i++){
            var concertDetail = new ConcertDetail(null,1L, tenMinutesLater,tenMinutesLater); //콘서트
            var concertDetail2 = new ConcertDetail(null,2L, now,now); //콘서
            var concertDetail3 = new ConcertDetail(null,3L, now,now); //콘서
            var concertDetail4 = new ConcertDetail(null,4L, now,now); //콘서
            details.add(concertDetail);
            details.add(concertDetail2);
            details.add(concertDetail3);
            details.add(concertDetail4);
        }
        var concertDetail2 = new ConcertDetail(null,2L, tenMinutesLater,tenMinutesLater); //콘서
        var concertDetail3 = new ConcertDetail(null,3L, tenMinutesLater,tenMinutesLater); //콘서
        details.add(concertDetail2);
        details.add(concertDetail3);
        concertDetailService.saveAllConcertDetail(details);


    }

    @Test
    @DisplayName("예약 가능날짜 가져오는 로직.")
    public  void getAbleDatesTest(){

        long startTime = System.currentTimeMillis();
        System.out.println("Test started at: " + startTime + " ms");
        //given
        var result = concertFacade.getAbleDates(1L);

        System.out.println("=====>size = "+ result.size());
        assertThat(result.get(0).getConcertId()).isEqualTo(1L);  //then
        assertThat(result.get(1).getConcertId()).isEqualTo(1L);

        long endTime = System.currentTimeMillis();
        System.out.println("Test ended at: " + endTime + " ms");
        // 총 소요 시간 출력
        System.out.println("Test duration: " + (endTime - startTime) + " ms");
    }


}