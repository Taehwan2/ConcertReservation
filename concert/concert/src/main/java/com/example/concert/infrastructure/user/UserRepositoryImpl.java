package com.example.concert.infrastructure.user;

import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.service.UserRepository;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
     //실제  UserRepository 구현 클래스
    private final UserJpaRepository userJpaRepository;
    //포인트가 바뀐 사용자를 저장하는 메서드
    @Override
    public User saveUser(User userPoint) {
        return userJpaRepository.save(userPoint);
    }
   //해당 유저의 포인트를 받기위해 호출하는 메서드
    @Override
    public User getUserPoint(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(()->new BusinessBaseException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public User getUserPointWithLock(Long userId) {
        return userJpaRepository.findByIdWithLock(userId).orElseThrow(()->new BusinessBaseException(ErrorCode.USER_NOT_FOUND));
    }

    @Override
    public int updateUserPoint(User userPoint) {
        return userJpaRepository.updateUserPoint(userPoint.getUserId(),userPoint.getPoint());
    }
}
