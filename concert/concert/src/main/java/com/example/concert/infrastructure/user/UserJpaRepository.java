package com.example.concert.infrastructure.user;

import com.example.concert.domain.user.entity.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

//실제 구현체
public interface UserJpaRepository extends JpaRepository<User,Long> {

    //이 로직은 실패하면 안되는 로직이기 때문에 비관적락에 가장 잘 어울리고, 낙관적 락으로 재시도를 구현해서
    //완벽하게 처리한다고 해도 비관적락 보다 훨씬 오래걸림을 알 수 있음 그래서 낙관적 락을 통해서 구현했음.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select u from User u where u.userId = :userId")
    Optional<User> findByIdWithLock(@Param("userId") Long userId);

    @Modifying
    @Query("update User u set u.point =:point where u.userId =:userId")
    int updateUserPoint(@Param("userId") Long userId, @Param("point") BigDecimal point);
}
