package com.example.concert.domain.user.service;

import com.example.concert.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

   // 유저에 맞는 포인트를 가져오는 로직
   public User getUserPoint(Long userId){
       return  userRepository.getUserPoint(userId);
   }
    //이번 과제의 핵심 비관적 락을 거는 메서드  
    public User getUserWithLock(Long userId){return userRepository.getUserPointWithLock(userId);}
    //유저를 저장하는 로직
    public User save(User userPoint) {
        return userRepository.saveUser(userPoint);
    }
    public int updateUserPoint(User userPoint) {
        return userRepository.updateUserPoint(userPoint);
    }

    //가장 작은 단위로 유저를 조회하고 포인트를 변경하는 것을 트랜잭션으로 묶음
    @Transactional
    public int calculate(Long userId, BigDecimal amount) throws Exception {
        var userPoint = getUserWithLock(userId);
        userPoint.calculate(amount);
        //비관적락을 획득한 순서로 부터 순차로 업데이트
        return updateUserPoint(userPoint);
    }

}

