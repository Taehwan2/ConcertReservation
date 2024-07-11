package com.example.concert.Application;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "결제 내역")
public class Payment {
    @Schema(description = "성공 실패")
    private boolean check;

}
