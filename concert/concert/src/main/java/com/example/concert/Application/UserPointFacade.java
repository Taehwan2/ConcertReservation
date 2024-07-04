package com.example.concert.Application;

import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.domain.point.entity.User;
import com.example.concert.domain.pointHistory.PointHistory;
import org.springframework.stereotype.Service;

@Service
public class UserPointFacade {

    public User getUserPoint(Long userId){
        return new User();
    }

    public PointHistory changePoint(PointRequest userId){
        return new PointHistory();
    }
}
