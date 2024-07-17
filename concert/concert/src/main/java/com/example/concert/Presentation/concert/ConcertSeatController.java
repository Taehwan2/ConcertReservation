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

    @GetMapping("/concert/seats/{concertDetailId}")
    public List<ConcertSeatResponse> getAbleSeats(@PathVariable(name = "concertDetailId")Long concertDetailId) throws Exception {
        var seats  =  userSeatFacade.getAbleSeats(concertDetailId);
        return seats.stream()
                .map(ConcertSeat::entityToResponse)
                .collect(Collectors.toList());
    }

    @PatchMapping("/concert/seat")
    public ConcertSeatResponse enrollSeats(@RequestBody ConcertSeatRequest concertSeatRequest) throws Exception {
        var seat  =  userSeatFacade.reserveSeats(concertSeatRequest);
        return ConcertSeat.entityToResponse(seat);

    }
}
