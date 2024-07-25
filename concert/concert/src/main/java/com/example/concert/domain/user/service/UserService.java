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

    public User getUserWithLock(Long userId){return userRepository.getUserPointWithLock(userId);}
    //유저를 저장하는 로직
    public User save(User userPoint) {
        return userRepository.saveUser(userPoint);
    }
    public int updateUserPoint(User userPoint) {
        return userRepository.updateUserPoint(userPoint);
    }


    @Transactional
    public int calculate(Long userId, BigDecimal amount) throws Exception {
        var userPoint = getUserWithLock(userId);
        userPoint.calculate(amount);
        //계산된 포인트를 저장하고 결과를  pointHistory 저장하는 로직
        return updateUserPoint(userPoint);
    }

}

