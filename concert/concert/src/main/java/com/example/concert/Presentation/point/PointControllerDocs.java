package com.example.concert.Presentation.point;

import com.example.concert.Presentation.point.model.PointHistoryResponse;
import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.Presentation.point.model.PointResponse;
import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "포인트", description = "포인트 조회 및 충전/사용 기능")
public interface PointControllerDocs {
    @Parameter(name = "userId" ,description = "사용자 아이디")
    @Operation(summary = "포인트 조회", description = "포인트조회 사용자아이디 필요")
    @ApiResponses(value ={
     @ApiResponse(responseCode = "201", description="포인트 반환",content = @Content(schema =
     @Schema(implementation = PointResponse.class))),
     @ApiResponse(responseCode = "404",description = "사용자 없음 발생가능  UserNotFound 예외" )
    })
    PointResponse lookupPoint(Long userId);


    @Operation(summary = "포인트 충전/사용", description = "포인트 history")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "201", description="포인트 내역 반환",content = @Content(schema =
            @Schema(implementation = PointHistoryResponse.class))),
            @ApiResponse(responseCode = "404",description = "사용자 없음 발생가능, 포인트 사용 실패 가능 UserNotFound 예외" )
    })
    PointHistoryResponse changePoint(PointRequest pointRequest) throws Exception;
}
