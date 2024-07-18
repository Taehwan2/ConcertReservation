package com.example.concert.domain.user.service;

import com.example.concert.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
   // 유저에 맞는 포인트를 가져오는 로직
    public User getUserPoint(Long userId){
        return  userRepository.getUserPoint(userId);
    }
   //유저를 저장하는 로직
    public User save(User userPoint) {
        return userRepository.saveUser(userPoint);
    }
}

