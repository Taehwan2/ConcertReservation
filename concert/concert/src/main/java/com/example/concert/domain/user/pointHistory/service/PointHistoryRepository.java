package com.example.concert.domain.user.pointHistory.service;

import com.example.concert.domain.user.pointHistory.entity.PointHistory;

public interface PointHistoryRepository {
    PointHistory savePointHistory(PointHistory history);
}
