package com.example.concert.domain.queue.entitiy;

import com.example.concert.Presentation.concert.model.queue.QueueResponse;
import com.example.concert.common.BaseEntity;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "queue", uniqueConstraints = {
        @UniqueConstraint(name = "userId_status", columnNames = {"user_id", "user_status"})},
                indexes = {
                        @Index(name = "idx_user_status", columnList = "user_status"),
                        @Index(name = "idx_userId_userStatus", columnList = "user_id, user_status"),
                        @Index(name = "idx_userId", columnList = "user_id")
                }
)    //대기열을 검증할 때 유저번호와 대기열 상태값으로 유니크 조건을 걸어 놓으면 한 유저가 같은 상태를 가질 수 없다.
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor           //대기열 엔티티
public class Queue extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queueId;
    //사용자 아이디
    private Long userId;
    //사용자의 대기번호
    private int waitingNumber;
    //대기열의 상태 wait,work,expired
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;
    //만료되었는지
    private boolean expired;
    //만료 시간
    private LocalDateTime expiredAt;

    public Queue(long l, long l1, int i, UserStatus waiting, LocalDateTime now) {
        super();
    }

    //todo 어짜피 큐는 어떤 콘서트든 홈페이지 입장시 대기열을 받고 , 콘서트에 영향을 안받기에 concertId 삭제.

    public static QueueResponse entityToResponse(Queue queue) {
        return QueueResponse.builder()
                .queueId(queue.getQueueId())
                .userId(queue.getUserId())
                .waitingNumber(queue.waitingNumber)
                .userStatus(queue.getUserStatus())
                .expired(queue.isExpired())
                .expiredAt(queue.getExpiredAt())
                .build();
    }

    //사용자가 대기번호를 조회할때 대기번호를 갱신해주는 도메인 서비스
    public void updateWaitingNumber(int ranking) {
        this.waitingNumber = ranking;
    }

    //사용자가 대기열에 입장할때 이미 존재하는 대기번호가 있을 경우 도메인 서비스
    public void alreadyWait() throws Exception {
        if (this.userStatus.equals(UserStatus.WAITING))
            throw new BusinessBaseException(ErrorCode.QUEUE_ALREADY_WAITING);
    }

    //사용자가 대기열에 입장할 때 이미 실행하는 서비스가 있을 경우 도메인 서비스
    public void alreadyWorking() throws Exception {
        String errMessage = String.format("it will be expired at [%s]", this.getExpiredAt());
        if (this.userStatus.equals(UserStatus.WORKING))
            throw new BusinessBaseException(ErrorCode.QUEUE_ALREADY_WORKING, errMessage);
    }

    //대기상태를 설정해주는 서비스
    public void setWait() {
        this.userStatus = UserStatus.WAITING;
    }

    //실행 상태로 주고, 만료 시간을 입력해주는 서비스
    public void setWorking(int queue_expired_time) {
        this.userStatus = UserStatus.WORKING;
        this.expiredAt = LocalDateTime.now().plus(Duration.ofMinutes(queue_expired_time));
    }

    //*만료시간이 다 되었을 경우에 유니크 제약조건에 걸리기때문에 상태값을 없애주고 만료 여부만 남겨주는 부분*
    public void expiry() {
        this.userStatus = null;
        this.expired = true;
    }
}
