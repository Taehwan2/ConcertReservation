package com.example.concert.Presentation.concert.swaggerController;

import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@Tag(name = "좌석",description = "좌석 예약가능 번호, 좌석 예약")
public interface ConcertSeatDocsController {

    @Parameter(name = "concertDetailId" ,description = "userId 아이디")
    @Operation(summary = "예약 가능 좌석 조회", description = "예약가능한 좌석을 조회한다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description="예약 가능 좌석",content = @Content(schema =
            @Schema(implementation = ConcertSeatResponse.class))),
            @ApiResponse(responseCode = "404", description = " CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND,\"C0\",\"콘서트가 없습니다.\"),")
    })
    List<ConcertSeatResponse> getAbleSeats(Long concertDetailId) throws  Exception;

    @Operation(summary = "좌석 예약", description = "좌석을 예약하는 프로세스")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description="좌석 등록 성공",content = @Content(schema =
            @Schema(implementation = ConcertSeatResponse.class))),
            @ApiResponse(responseCode = "404", description = "    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND,\"S0\",\"좌석을 찾을 수 없습니다.\"),\n" +
                    "    SEAT_NO_INVALID(HttpStatus.BAD_REQUEST,\"S1\",\"잘못된 좌석번호입니다.\"),")
    })
    ConcertSeatResponse enrollSeats(@RequestBody ConcertSeatRequest concertSeatRequest) throws Exception;



}
