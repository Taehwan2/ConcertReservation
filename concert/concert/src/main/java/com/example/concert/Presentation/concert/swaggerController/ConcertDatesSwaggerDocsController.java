package com.example.concert.Presentation.concert.swaggerController;

import com.example.concert.Presentation.concert.model.date.DatesResponse;
import com.example.concert.Presentation.point.model.PointHistoryResponse;
import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.Presentation.point.model.PointResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
@Tag(name = "예약 가능 날짜",description = "콘서트 예약 가능한 날짜 구하는 API")
public interface ConcertDatesSwaggerDocsController {

    @Parameter(name = "concertId" ,description = "concert 아이디")
    @Operation(summary = "날짜 수신", description = "예약가능한 날짜를 수신한다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description="getDates",content = @Content(schema =
            @Schema(implementation = DatesResponse.class))),
            
            @ApiResponse(responseCode = "404", description = "콘서트 없음 발생가능")
    })
    List<DatesResponse> getAbleDates(Long concertId);


}
