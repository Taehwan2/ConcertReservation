/*
package com.example.concert.common;

import com.example.concert.domain.queue.service.QueueService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;


@RequiredArgsConstructor
public class QueueCheckInterceptor implements HandlerInterceptor {

    private final QueueService queueService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userQueueStringId = request.getHeader("Authorization");
        if(userQueueStringId==null){
            throw new Exception("No userId In Queue");
        }
        boolean statusGoing = queueService.isWorking(Long.valueOf(userQueueStringId));
        if(!statusGoing){
            throw new Exception("Not working In Queue");
        }
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
*/
