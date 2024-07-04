package com.example.concert.Presentation.concert;

import com.example.concert.Application.ConcertFacade;
import com.example.concert.Presentation.concert.model.date.DatesResponse;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatResponse;
import com.example.concert.domain.concertSeat.entity.ConcertSeat;
import com.example.concert.domain.concertdetail.ConcertDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ConcertDatesController {
    private final ConcertFacade concertFacade;

    @GetMapping("/concert/reservation/days/{concertId}")
    public List<DatesResponse> getAbleSeats(@PathVariable(name = "concertId")Long concertDetailId){
        var dates  =  concertFacade.getAbleDates(concertDetailId);
        return dates.stream()
                .map(ConcertDetail::entityToResponse)
                .collect(Collectors.toList());
    }
}
