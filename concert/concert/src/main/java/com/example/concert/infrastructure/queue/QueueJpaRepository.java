package com.example.concert.infrastructure.queue;

import com.example.concert.domain.queue.entitiy.Queue;
import com.example.concert.domain.queue.entitiy.UserStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<Queue,Long> {

    Queue findByUserIdAndUserStatusIn(Long userId, List<UserStatus> waiting);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Queue q WHERE q.userStatus = :working")
    List<Queue> findStatusIsWorkingWithPessimisticLock(UserStatus working);

    Optional<Queue> findByUserId(Long userId);

    @Query(value = "SELECT rw.ranking " +
            "FROM ( " +
            "SELECT @rank := @rank + 1 AS ranking, queue_id " +
            "FROM queue, (SELECT @rank := 0) r " +
            "WHERE user_status = :userStatus " +
            "ORDER BY created_at " +
            ") AS rw " +
            "WHERE rw.queue_id = :waitId", nativeQuery = true)
    int getRanking(Long waitId,UserStatus userStatus);

    @Query("SELECT q FROM Queue q WHERE q.expiredAt <=:now and q.userStatus =:working  ")
    List<Queue> findExpiredQueueOnWorking(UserStatus working, LocalDateTime now);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT q FROM Queue q WHERE q.userStatus = :status ORDER BY q.createdAt")
    Page<Queue> findUserStatusWaitingLimitSize(@Param("status") UserStatus status, Pageable pageable);

    Optional<Queue> findByUserIdAndUserStatus(Long userId, UserStatus userStatus);
}
