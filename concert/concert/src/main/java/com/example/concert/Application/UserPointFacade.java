package com.example.concert.Application;

import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import com.example.concert.domain.user.pointHistory.enumType.PointType;
import com.example.concert.domain.user.pointHistory.service.PointHistoryService;
import com.example.concert.domain.user.service.UserService;
import com.example.concert.redis.LockService;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPointFacade {
    // 사용자 포인트에 관한 서비스와 그에 대한 결과를 저장하기 위한  서비스를  application 계층에 Facade 로 둔다.
    private final UserService userService;
    private final PointHistoryService pointHistoryService;

    private final LockService lockService;

    public User getUserPoint(Long userId){
        return userService.getUserPoint(userId);
    }
   //사용자의 포인트와 그 결과를 담기 위해서 Transactional 걸어서 메서드를 생성

    public PointHistory changePoint(PointRequest pointRequest) throws Exception {

        var history = PointHistory.builder().userId(pointRequest.getUserId()).amount(pointRequest.getCharge()).build();
        BigDecimal amount = pointRequest.getCharge();
        // 값을 확인하고 충전인지 사용인지를 확인하는  의 메서드
        history.checkType(amount);
       //userId 에 맞는 포인트를 가져와서 값을 계산하는 로직
        userService.calculate(pointRequest.getUserId(), amount);

        return pointHistoryService.save(history);
    }
}
