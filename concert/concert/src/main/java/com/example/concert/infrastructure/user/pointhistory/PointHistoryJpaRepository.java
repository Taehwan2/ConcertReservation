package com.example.concert.infrastructure.user.pointhistory;

import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
// 실제 구현체
public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
}
