package com.example.concert.Presentation.concert;

import com.example.concert.Application.ConcertFacade;
import com.example.concert.Presentation.concert.model.date.DatesResponse;
import com.example.concert.Presentation.concert.swaggerController.ConcertDatesSwaggerDocsController;
import com.example.concert.domain.concertdetail.entity.ConcertDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ConcertDatesController implements ConcertDatesSwaggerDocsController {
    private final ConcertFacade concertFacade;

    @GetMapping("/concert/reservation/days/{concertId}")
    public List<DatesResponse> getAbleDates(@PathVariable(name = "concertId")Long concertId){
        var dates  =  concertFacade.getAbleDates(concertId);
        return dates.stream()
                .map(ConcertDetail::entityToResponse)
                .collect(Collectors.toList());
    }
}
