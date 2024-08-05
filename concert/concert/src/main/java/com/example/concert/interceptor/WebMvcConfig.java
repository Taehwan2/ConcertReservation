package com.example.concert.interceptor;

import com.example.concert.domain.queue.service.QueueService;
import com.example.concert.domain.queue.service.RedisQueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
     //Web Mvc Config 에 지정한 Interceptor 를 포함하는  클래스
    private final RedisQueueService queueService; //디비 대기열 서비스에서 레디스 대기열 서비로 변경

  @Override
  //Todo 실제 대기열을 필요로 하는 을 설정해 주는 메서드.
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new QueueCheckInterceptor(queueService))
                .addPathPatterns(
                        "/concert/reservation/days/{concertId}",
                        "/concert/seats/{concertDetailId}",
                        "/concert/seat",
                        "/concert/reservation/days"
                );
    }
}