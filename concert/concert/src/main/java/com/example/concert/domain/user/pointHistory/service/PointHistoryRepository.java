package com.example.concert.domain.user.pointHistory.service;

import com.example.concert.domain.user.pointHistory.entity.PointHistory;
//계층을 위해서 리파지토리 인터페이스를 생성
public interface PointHistoryRepository {
    PointHistory savePointHistory(PointHistory history);
}
