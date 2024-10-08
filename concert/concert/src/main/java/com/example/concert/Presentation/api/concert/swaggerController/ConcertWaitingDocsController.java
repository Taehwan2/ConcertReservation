package com.example.concert.Presentation.concert.swaggerController;

import com.example.concert.Presentation.concert.model.date.DatesResponse;
import com.example.concert.Presentation.concert.model.queue.QueueRequest;
import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Tag(name = "대기열",description = "대기열 등록/조회 기능")
public interface ConcertWaitingDocsController {

    @Parameter(name = "userId" ,description = "userId 아이디")
    @Parameter(name = "waitId" ,description = "waitId 아이디")
    @Operation(summary = "대기열 번호 수신", description = "대기열 번호를 수신한다.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description="getQueue",content = @Content(schema =
            @Schema(implementation = QueueResponse.class))),
            @ApiResponse(responseCode = "404", description = "  QUEUE_NOT_FOUND(HttpStatus.NOT_FOUND,\\\"Q3\\\",\\\"대기열을 찾을 수 없습니다.\\\"),\\n\"")
    })
    Long getQueue(Long userId, Long waitId ) throws Exception;

    @Operation(summary = "대기열열 등록", description = "대기열 리퀘스트를 통해서 대기열 등록.")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description="대기열 등록성공",content = @Content(schema =
            @Schema(implementation = QueueResponse.class))),
            @ApiResponse(responseCode = "404", description = "QUEUE_ALREADY_WAITING(HttpStatus.CONFLICT, \"Q1\", \"이미 대기 중입니다.\"),\n" +
                    "    QUEUE_ALREADY_WORKING(HttpStatus.CONFLICT, \"Q2\", \"이미 작업 중입니다.\"),\n" +
                    "    QUEUE_NOT_FOUND(HttpStatus.NOT_FOUND,\"Q3\",\"대기열을 찾을 수 없습니다.\"),\n" +
                    "    QUEUE_NOT_WORKING(HttpStatus.NOT_FOUND,\"Q4\",\"현재 대기열을 통과하지 못했습니다.\"),")
    })
    boolean enrollQueue(@RequestBody QueueRequest queueRequest) throws Exception;
}
