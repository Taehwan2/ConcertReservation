package com.example.concert.interceptor;

import com.example.concert.domain.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final QueueService queueService;

  @Override
  //Todo 실제 메인 로직이 담긴 코드들
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