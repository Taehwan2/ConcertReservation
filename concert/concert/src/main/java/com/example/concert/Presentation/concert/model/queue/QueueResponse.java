package com.example.concert.Presentation.concert.model.queue;

import com.example.concert.domain.queue.entitiy.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class QueueResponse {
    private Long queueId;

    private Long userId;

    private Long waitNum;

    private UserStatus userStatus;

    private LocalDateTime expiredAt;
}
