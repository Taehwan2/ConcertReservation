package com.example.concert.Presentation.point;

import com.example.concert.Application.UserPointFacade;
import com.example.concert.Presentation.point.model.PointHistoryResponse;
import com.example.concert.Presentation.point.model.PointRequest;
import com.example.concert.Presentation.point.model.PointResponse;
import com.example.concert.domain.user.entity.User;
import com.example.concert.domain.user.pointHistory.entity.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/point")
public class PointController implements PointControllerDocs {
    // userPointFacade 를 사용해서 API 생성
    private final UserPointFacade userPointFacade;
    //유저 포인트 조회 에이피아이
    @GetMapping("/{userId}")
    public PointResponse lookupPoint(@PathVariable(name = "userId") Long userId){
        var userPoint = userPointFacade.getUserPoint(userId);
        return User.entityToResponse(userPoint);
    }
     //포인트 사용 충전 에이피아이
    @PatchMapping("")
    public PointHistoryResponse changePoint(@RequestBody PointRequest pointRequest) throws Exception {
        var point = userPointFacade.changePoint(pointRequest);
        return PointHistory.entityToResponse(point);
    }


}
