package com.example.concert.Application;

import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import com.example.concert.domain.user.pointHistory.enumType.PointType;
import com.example.concert.domain.user.pointHistory.service.PointHistoryService;
import com.example.concert.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserPointFacade {

    private final UserService userService;
    private final PointHistoryService pointHistoryService;

    public User getUserPoint(Long userId){
        return userService.getUserPoint(userId);
    }

    @Transactional
    public PointHistory changePoint(PointRequest pointRequest) throws Exception {
        var history = PointHistory.builder().userId(pointRequest.getUserId()).amount(pointRequest.getCharge()).build();
        BigDecimal amount = pointRequest.getCharge();

        history.checkType(amount);

        var userPoint = getUserPoint(pointRequest.getUserId());
        userPoint.calculate(amount);

        userService.save(userPoint);
        return pointHistoryService.save(history);
    }
}
