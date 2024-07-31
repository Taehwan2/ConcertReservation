package com.example.concert.domain.concertdetail.service;

import com.example.concert.Presentation.concert.model.concert.ConcertDetailReq;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EnableCaching
class ConcertDetailCacheServiceTest {
    @MockBean
    private ConcertDetailRepository concertDetailRepository;

    @Autowired
    private ConcertDetailService concertDetailService;

    @Autowired
    private CacheManager cacheManager;
    @Test
    @DisplayName("concertDetailId가 1L인 객체를 한번조회하고 캐시에 저장해서 hit가 발생하여 repository 횟수가 1로 유지되는 테스트")
    void getConcertDetail() {
        Long concertDetailId = 1L;
        ConcertDetail concertDetail = new ConcertDetail();
        concertDetail.setConcertDetailId(concertDetailId);

        //given
        given(concertDetailRepository.getConcert(concertDetailId)).willReturn(concertDetail);

        //when&then
        // 첫 번째 호출: 캐시 미스
        ConcertDetail result1 = concertDetailService.getConcertDetail(concertDetailId);
        verify(concertDetailRepository, times(1)).getConcert(concertDetailId);

        // 두 번째 호출: 캐시 히트
        ConcertDetail result2 = concertDetailService.getConcertDetail(concertDetailId);
        verify(concertDetailRepository, times(1)).getConcert(concertDetailId); // 호출 횟수가 증가하지 않음

        ConcertDetail result3 = concertDetailService.getConcertDetail(concertDetailId);
        verify(concertDetailRepository, times(1)).getConcert(concertDetailId); // 호출 횟수가 증가하지 않음
    }

    @Test
    @DisplayName("concertDetailId가 1인 객체를 삭제했을 때 캐시에서 지워지는 지 확인하는 테스트")
    void deleteConcertDetail() {
        Long concertDetailId = 1L;
        ConcertDetail concertDetail = new ConcertDetail();
        concertDetail.setConcertDetailId(concertDetailId);

        //given
        given(concertDetailRepository.getConcert(concertDetailId)).willReturn(concertDetail);

        //when&then
        // 캐시에 저장
        concertDetailService.getConcertDetail(concertDetailId);
        verify(concertDetailRepository, times(1)).getConcert(concertDetailId);

        // 캐시에서 삭제
        concertDetailService.deleteConcertDetail(concertDetailId);
        assertThat(cacheManager.getCache("concertDetail").get(concertDetailId)).isNull();
    }

    @Test
    @DisplayName("concertDetailId가 1인 객체의 시작시간과 예약가능시간을 바꿨을 때 캐시가 업데이트 되는지 확인하는 테스트")
    void updateConcert() {
        Long concertDetailId = 1L;
        ConcertDetail concertDetail = new ConcertDetail();
        concertDetail.setConcertDetailId(concertDetailId);
        concertDetail.setStartDate(LocalDateTime.now());
        concertDetail.setReservationStartDate(LocalDateTime.now().plusDays(1));

        //given
        given(concertDetailRepository.getConcert(concertDetailId)).willReturn(concertDetail);
        given(concertDetailRepository.saveConcertDetail(concertDetail)).willReturn(concertDetail);

        ConcertDetailReq concertDetailReq = new ConcertDetailReq();
        concertDetailReq.setStartDate(LocalDateTime.now().plusDays(2));
        concertDetailReq.setReservationStartDate(LocalDateTime.now().plusDays(3));

        //when&then
        concertDetailService.getConcertDetail(concertDetailId);
        verify(concertDetailRepository, times(1)).getConcert(concertDetailId);

        concertDetailService.updateConcert(concertDetailId, concertDetailReq);

        System.out.println(cacheManager.getCache("concertDetail").get(concertDetailId).get());

    }
}