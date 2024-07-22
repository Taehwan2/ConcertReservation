package com.example.concert.infrastructure.queue;

import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<Queue,Long> {
      //대기중인 대기열을 가져오는 코드
    Queue findByUserIdAndUserStatusIn(Long userId, List<UserStatus> waiting);
    //실행중인 대기열들을 가져오는 코드 순차적으로 처리하기위해 비관적 락을 건다.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Queue q WHERE q.userStatus = :working")
    List<Queue> findStatusIsWorkingWithPessimisticLock(UserStatus working);
   //사용자 아이디로 검색
    Optional<Queue> findByUserId(Long userId);
   //사용자의 대기 순번을 가져오는 코드

    @Query(value = "SELECT ranking " +
            "FROM ( " +
            "SELECT row_number() OVER (ORDER BY created_at) AS ranking, queue_id " +
            "FROM queue " +
            "WHERE user_status = :userStatus " +
            ") AS ranked_waiting " +
            "WHERE queue_id = :waitId", nativeQuery = true)
            int getRanking(@Param("waitId") Long waitId, @Param("userStatus") String userStatus);
   //대기열이 만료된 대기열을 가져오는 코드
    @Query("SELECT q FROM Queue q WHERE q.expiredAt <=:now and q.userStatus =:working  ")
    List<Queue> findExpiredQueueOnWorking(UserStatus working, LocalDateTime now);
   //순서대로 갱신시켜줄 데이터를 가져오는 쿼리
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Queue q WHERE q.userStatus = :status ORDER BY q.createdAt")
    Page<Queue> findUserStatusWaitingLimitSize(@Param("status") UserStatus status, Pageable pageable);
    //상태를 통해 가져오는 코드
    Optional<Queue> findByUserIdAndUserStatus(Long userId, UserStatus userStatus);
}
