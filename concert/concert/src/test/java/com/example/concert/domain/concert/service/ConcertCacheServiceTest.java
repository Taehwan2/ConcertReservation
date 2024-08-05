package com.example.concert.domain.concert.service;

import com.example.concert.Presentation.concert.model.concert.ConcertDetailReq;
import com.example.concert.Presentation.concert.model.concert.ConcertReq;
import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import com.example.concert.domain.concertdetail.service.ConcertDetailRepository;
import com.example.concert.domain.concertdetail.service.ConcertDetailService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.retry.annotation.EnableRetry;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
//캐시태스트
@SpringBootTest
@EnableRetry
class ConcertCacheServiceTest {
    @MockBean
    private ConcertRepository concertRepository;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private CacheManager cacheManager;
    @Test
    @DisplayName("concertId가 1L인 객체를 한번조회하고 캐시에 저장해서 hit가 발생하여 repository 횟수가 1로 유지되는 테스트.")
    void getConcert() {
        Long concertId = 1L;
        Concert concert = new Concert();
        concert.setConcertId(concertId);
        //given.
        given(concertRepository.getConcert(concertId)).willReturn(concert);


        //when&then
        // 첫 번째 호출: 캐시 미스.
        Concert result1 = concertService.getConcert(concertId);
        verify(concertRepository, times(1)).getConcert(concertId);

        // 두 번째 호출: 캐시 히트.
        Concert result2 = concertService.getConcert(concertId);
        verify(concertRepository, times(1)).getConcert(concertId);

        Concert result3 = concertService.getConcert(concertId);
        verify(concertRepository, times(1)).getConcert(concertId);
    }

    @Test
    @DisplayName("concert 이름과 장르를 amundi와 action으로 변경하고 변경된 내용이 캐시에 갱신되는지 확인하는 테스트.")
    void updateConcert() {
        Long concertId = 1L;
        Concert concert = new Concert();
        concert.setConcertId(concertId);
        concert.setName("amundi");
        concert.setGenre("action");

        //given.
        given(concertRepository.getConcert(concertId)).willReturn(concert);
        given(concertRepository.saveConcert(concert)).willReturn(concert);

        ConcertReq concertReq = new ConcertReq();
        concertReq.setGenre("amundi");
        concertReq.setGenre("action");

        //when&then.
        concertService.getConcert(concertId);
        verify(concertRepository, times(1)).getConcert(concertId);

        concertService.updateConcert(concertId, concertReq);

        System.out.println(cacheManager.getCache("concerts").get(concertId).get());
    }

    @Test
    @DisplayName("concertId가 1인 객체를 삭제했을 때 캐시에서 지워지는 지 확인하는 테스트.")
    void deleteConcert() {
        Long concertId = 1L;
        Concert concert = new Concert();
        concert.setConcertId(concertId);

        //given.
        given(concertRepository.getConcert(concertId)).willReturn(concert);

        // 캐시에 저장.
        //when&then.
        concertService.getConcert(concertId);
        verify(concertRepository, times(1)).getConcert(concertId);

        // 캐시에서 삭제.
        concertService.deleteConcert(concertId);
        assertThat(cacheManager.getCache("concertDetail").get(concertId)).isNull();
    }
}