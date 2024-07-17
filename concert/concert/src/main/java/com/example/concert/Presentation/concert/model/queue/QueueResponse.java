package com.example.concert.Presentation.concert.model.queue;

import com.example.concert.domain.queue.entitiy.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "대기열 반환")
public class QueueResponse {
    @Schema(description = "대기열 아이디")
    private Long queueId;

    @Schema(description = "유저 번호")
    private Long userId;

    @Schema(description = "대기번호")
    private int waitingNumber;

    @Schema(description = "대기 상태")
    private UserStatus userStatus;

    @Schema(description = "만료시간")
    private LocalDateTime expiredAt;

    @Schema(description = "만료여부")
    private boolean expired;


}
