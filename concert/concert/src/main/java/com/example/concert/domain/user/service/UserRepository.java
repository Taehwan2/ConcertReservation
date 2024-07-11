package com.example.concert.domain.user.service;

import com.example.concert.domain.user.entity.User;

public interface UserRepository {
     User saveUser(User userPoint);

    User getUserPoint(Long userId);
}
