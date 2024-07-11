package com.example.concert.infrastructure.user.pointhistory;

import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
}
