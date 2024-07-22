package com.example.concert.filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//Todo LogFilter 등록 실제 필터가 동작하기 위해 Spring 에 FILTER 를 등록해진다.
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<LogFilter> loggingFilter() {
        //Todo Bean 으로 등록
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogFilter());
        //Todo 모든 패턴 등록
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}