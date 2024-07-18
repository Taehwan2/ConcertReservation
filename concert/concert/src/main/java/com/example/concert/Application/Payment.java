package com.example.concert.Application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
//결제 실패 서공 유무를 나타내는 dto
@Getter
@Builder
@Schema(description = "결제 내역")
public class Payment {
    @Schema(description =  "성공 실패")
    private boolean check; //체크 변수

}
