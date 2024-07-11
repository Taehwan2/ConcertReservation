package com.example.concert.domain.user.pointHistory.service;

import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryService {
    private final  PointHistoryRepository pointHistoryRepository;


    public PointHistory save(PointHistory history) {
        return pointHistoryRepository.savePointHistory(history);
    }
}
