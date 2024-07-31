package com.example.concert.domain.concert.service;

import com.example.concert.Presentation.concert.model.concert.ConcertReq;
import com.example.concert.domain.concert.entity.Concert;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {
    private final ConcertRepository concertRepository;
    //콘서트 저장 로직
    public Concert saveConcert(Concert concert){
        return concertRepository.saveConcert(concert);
    }

   //cache 로 hit를 한다면 캐시서버에서 데이터를 가져오고 hit하지 못한다면 데이터를 DB에서 끌고온다..
   //이 부분을 Cache로 사용한 이유는 콘서트 정보는 자주 바뀌는 정보가 아니기 때문이다..

   @Cacheable(value = "concerts", key = "#concertId")

    public Concert getConcert(Long concertId){
        return concertRepository.getConcert(concertId);
    }


    //콘서트 정보가 바뀌면 캐시가 삭제되는것이 맞으나 콘서트 정보가 삭제되는 것이 아니라 바뀌는것이므로..
    //콘서트 정보가 바뀌면 기존 캐시를 갱신해준다..
    @CachePut(value = "concerts", key = "#id")
    public Concert updateConcert(Long id , ConcertReq concertReq){

        var findConcert = concertRepository.getConcert(id);

        findConcert.setName(concertReq.getName());

        findConcert.setGenre(concertReq.getGenre());

         return concertRepository.saveConcert(findConcert);
    }

    //만약 콘서트의 정보가 지워질 경우에는 Cache를 삭제하는 로직을 담고있다..
    //콘서트의 정보가 없다면 캐시가 남아있다면 큰 오류사항이기 때문이다..
    @CacheEvict(value = "concerts", key = "#concertId")

    public void deleteConcert(Long  concertId){ concertRepository.deleteConcert(concertId);

    }

}
