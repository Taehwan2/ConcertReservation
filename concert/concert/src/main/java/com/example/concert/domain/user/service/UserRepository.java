package com.example.concert.domain.user.service;

import com.example.concert.domain.user.entity.User;
//유저 레파지토리 인터페이스 계층구조를 위해서 선언한후 도메인 계층에 올려둠
public interface UserRepository {
    User saveUser(User userPoint);
    // 서비스에서 사용하는 기능들

    User getUserPoint(Long userId);
    //유저의 포인트를 업데이트하는 메서드
    int updateUserPoint(User userPoint);
     //포인트 조회에 비관적락 거는 메서드
    User getUserPointWithLock(Long userId);
}
