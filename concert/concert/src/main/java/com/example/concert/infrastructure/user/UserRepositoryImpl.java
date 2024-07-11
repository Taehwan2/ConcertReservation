package com.example.concert.infrastructure.user;

import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.service.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public User saveUser(User userPoint) {
        return userJpaRepository.save(userPoint);
    }

    @Override
    public User getUserPoint(Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(()->new NoSuchElementException("UserNotFound"));
    }
}
