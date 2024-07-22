package com.example.concert.Presentation.concert;

import com.example.concert.Application.UserSeatFacade;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatResponse;
import com.example.concert.Presentation.concert.swaggerController.ConcertSeatDocsController;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ConcertSeatController implements ConcertSeatDocsController {

    private final UserSeatFacade userSeatFacade;
    //예약 가능한 좌석을 찾는 Api 생성
    @GetMapping("/concert/seats/{concertDetailId}")
    public List<ConcertSeatResponse> getAbleSeats(@PathVariable(name = "concertDetailId")Long concertDetailId) throws Exception {
        var seats  =  userSeatFacade.getAbleSeats(concertDetailId); //Appliation 계층의 파사드를 통해서 검증
        return seats.stream()
                .map(ConcertSeat::entityToResponse)
                .collect(Collectors.toList());
    }
   //좌석번호를 가지고 좌석 등록
    @PatchMapping("/concert/seat")
    public ConcertSeatResponse enrollSeats(@RequestBody ConcertSeatRequest concertSeatRequest) throws Exception {
        var seat  =  userSeatFacade.reserveSeats(concertSeatRequest); //Appliation 계층의 파사드를 통해서 검증
        return ConcertSeat.entityToResponse(seat);

    }
}
