package com.example.concert.Presentation.point;

import com.example.concert.Application.UserPointFacade;
import com.example.concert.Presentation.point.model.PointHistoryResponse;
import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.Presentation.point.model.PointResponse;
import com.example.concert.domain.point.entity.User;
import com.example.concert.domain.pointHistory.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/point")
public class PointController {

    private final UserPointFacade userPointFacade;

    @GetMapping("/{userId}")
    public PointResponse lookupPoint(@PathVariable(name = "userId") Long userId){
        var userPoint = userPointFacade.getUserPoint(userId);
        return User.entityToResponse(userPoint);
    }

    @PatchMapping("")
    public PointHistoryResponse changePoint(@RequestBody PointRequest pointRequest){
        var point = userPointFacade.changePoint(pointRequest);
        return PointHistory.entityToResponse(point);
    }


}
