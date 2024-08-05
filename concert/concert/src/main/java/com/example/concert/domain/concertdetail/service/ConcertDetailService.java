package com.example.concert.domain.concertdetail.service;

import com.example.concert.Presentation.concert.model.concert.ConcertDetailReq;
import com.example.concert.Presentation.concert.model.concert.ConcertReq;
import com.example.concert.domain.concert.entity.Concert;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertDetailService {
    private final ConcertDetailRepository concertDetailRepository;

   //콘서트 옵션을 저장하는 코드
    public ConcertDetail saveConcertDetail(ConcertDetail concertDetail){
        return concertDetailRepository.saveConcertDetail(concertDetail);
    }

    //cache 로 hit를 한다면 캐시서버에서 데이터를 가져오고 hit하지 못한다면 데이터를 DB에서 끌고온다..
    //이 부분을 Cache로 사용한 이유는 콘서트 옵션 정보는 자주 바뀌는 정보가 아니기 때문이다..
    @Cacheable(value = "concertDetail", key = "#concertDetailId")

    public ConcertDetail getConcertDetail(Long concertDetailId){

        return concertDetailRepository.getConcert(concertDetailId);

    }

    //콘서트에서 예약가능한 날짜를 가져오는 콘서트 옵션의 서비스
    public List<ConcertDetail> getAbleDates(Long concertId) {
        return concertDetailRepository.getAbleDates(concertId);
    }

    //만약 콘서트의 정보가 지워질 경우에는 Cache를 삭제하는 로직을 담고있다..
    //콘서트의 정보가 없다면 캐시가 남아있다면 큰 오류사항이기 때문이다..
    @CacheEvict(value = "concertDetail", key = "#concertDetailId")
    public void deleteConcertDetail(Long concertDetailId){

        concertDetailRepository.deleteConcertDetail(concertDetailId);

    }
    //콘서트 옵션정보가 바뀌면 캐시가 삭제되는것이 맞으나 콘서트 옵션 정보가 삭제되는 것이 아니라 바뀌는것이므로.
    //콘서트 옵션 정보가 바뀌면 기존 캐시를 갱신해준다..
    @CachePut(value="concertDetail",key = "#concertDetailId")

    public ConcertDetail updateConcert(Long concertDetailId, ConcertDetailReq concertDetailReq) {

        var findConcertDetail = getConcertDetail(concertDetailId);

        findConcertDetail.setReservationStartDate(concertDetailReq.getReservationStartDate());

        findConcertDetail.setStartDate(concertDetailReq.getStartDate());

        return saveConcertDetail(findConcertDetail);
    }
}
