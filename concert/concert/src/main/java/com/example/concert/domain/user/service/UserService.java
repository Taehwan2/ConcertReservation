package com.example.concert.domain.user.service;

import com.example.concert.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getUserPoint(Long userId){
        return  userRepository.getUserPoint(userId);
    }

    public User save(User userPoint) {
        return userRepository.saveUser(userPoint);
    }
}

