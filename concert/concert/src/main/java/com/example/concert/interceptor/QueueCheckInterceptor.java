package com.example.concert.interceptor;

import com.example.concert.domain.queue.service.QueueService;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;


@RequiredArgsConstructor
public class QueueCheckInterceptor implements HandlerInterceptor {
   //Todo 대기열 검증을 위한 인터셉터
    private final QueueService queueService;

    //preHandle  컨트롤러에 가기전에 검증하는 로직
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userQueueStringId = request.getHeader("Authorization"); //Authentication 에서 유저의 아이디를 가져오는 코드
        if(userQueueStringId==null){      //만약 유저아이디가 없다면 대기열 에러
            throw new BusinessBaseException(ErrorCode.QUEUE_NOT_FOUND);
        }
        boolean statusGoing = queueService.isWorking(Long.valueOf(userQueueStringId));
        if(!statusGoing){ //만약 실행중이 아니라면 에러
            throw new BusinessBaseException(ErrorCode.QUEUE_NOT_WORKING);
        } //헨들러 역할 끝
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
