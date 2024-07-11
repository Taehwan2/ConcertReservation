package com.example.concert.domain.queue.entitiy;

import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import com.example.concert.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue", uniqueConstraints = {
        @UniqueConstraint(name = "userId_status", columnNames = {"user_id", "user_status"})
})
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Queue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;

    private Long userId;

    private int waitingNumber;

    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;


    private LocalDateTime expiredAt;

    public Queue(long l, long l1, int i, UserStatus waiting, LocalDateTime now) {
        super();
    }

    //todo 어짜피 큐는 어떤 콘서트든 영향을 안받기에 concertId 삭제

    public static QueueResponse entityToResponse(Queue queue) {
        return QueueResponse.builder()
                .queueId(queue.getQueueId())
                .userId(queue.getUserId())
                .waitingNumber(queue.waitingNumber)
                .userStatus(queue.getUserStatus())
                .expiredAt(queue.getExpiredAt())
                .build();
    }

    public void updateWaitingNumber(int ranking) {
        this.waitingNumber = ranking;
    }

    public void alreadyWait() throws Exception {
        if(this.userStatus.equals(UserStatus.WAITING))throw  new Exception("already wait");
    }

    public void alreadyWorking() throws Exception {
        String errMessage = String.format("it will be expired at [%s]",this.getExpiredAt());
        if(this.userStatus.equals(UserStatus.WORKING))throw new Exception("already working"+errMessage);
    }

    public void setWait() {
        this.userStatus = UserStatus.WAITING;
    }

    public void setWorking(int queue_expired_time) {
        this.userStatus = UserStatus.WORKING;
        this.expiredAt = LocalDateTime.now().plus(Duration.ofMinutes(queue_expired_time));
    }

    public void expiry() {
    this.userStatus =UserStatus.EXPIRED;
    }
}
