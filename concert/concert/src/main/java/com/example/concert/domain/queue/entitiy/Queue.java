package com.example.concert.domain.queue.entitiy;

import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "queue")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Queue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;

    private Long userId;

    private UserStatus userStatus;

    private LocalDateTime expiredAt;


    public static QueueResponse entityToResponse(Queue queue) {
        return QueueResponse.builder()
                .queueId(queue.getQueueId())
                .userId(queue.getUserId())
                .userStatus(queue.getUserStatus())
                .expiredAt(queue.getExpiredAt())
                .build();
    }
}
