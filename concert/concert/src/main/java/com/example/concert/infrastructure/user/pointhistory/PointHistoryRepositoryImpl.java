package com.example.concert.infrastructure.user.pointhistory;

import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import com.example.concert.domain.user.pointHistory.service.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {
    //실제  PointHistoryRepository  구현체
    private final PointHistoryJpaRepository pointHistoryJpaRepository;
   //실제 포인트 충전/사용 값을 저장하는 로직
    @Override
    public PointHistory savePointHistory(PointHistory history) {
        return pointHistoryJpaRepository.save(history);
    }
}
