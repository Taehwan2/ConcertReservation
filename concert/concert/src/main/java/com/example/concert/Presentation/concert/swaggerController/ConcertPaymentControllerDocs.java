package com.example.concert.Presentation.concert.swaggerController;

import com.example.concert.Application.Payment;
import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import com.example.concert.Presentation.concert.model.seat.ConcertSeatRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "결제",description = "결제 요청하는 api")
public interface ConcertPaymentControllerDocs {

    @Operation(summary = "결제 요청", description = "결제를 요청한다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description="getQueue",content = @Content(schema =
            @Schema(implementation = Payment.class))),
            @ApiResponse(responseCode = "404", description = "결제 실패")
    })
    Payment payment( ConcertSeatRequest concertSeatRequest) throws Exception;
}
