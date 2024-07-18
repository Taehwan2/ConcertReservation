package com.example.concert.domain.user.pointHistory.service;

import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
    private final  PointHistoryRepository pointHistoryRepository;

   //실제로 도메인이 사용하는 서비스
    public PointHistory save(PointHistory history) {
        return pointHistoryRepository.savePointHistory(history);
    }
}
