package com.example.concert.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
@Slf4j
//Todo HttpRequest 와 HttpsResponse 의 로그를 찍기 위한 class
public class LogFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Todo ContentCachingRequestWrapper 로 Request 감싸기
        ContentCachingRequestWrapper httpServletRequest = new ContentCachingRequestWrapper((HttpServletRequest) request);
        //Todo  ContentCachingResponseWrapper 로 Response 감싸기
        ContentCachingResponseWrapper httpServletResponse = new ContentCachingResponseWrapper((HttpServletResponse) response);

        //Todo chain doFilter 실행
        chain.doFilter(httpServletRequest,httpServletResponse);

        //Todo Request 의 URL 정보 가져오기
        String uri = httpServletRequest.getRequestURI();
        String reqContent = new  String(httpServletRequest.getContentAsByteArray());
        log.info("uri : {}, request : {}", uri, reqContent);

        //Todo Response 의 body 정보 가져오기
        int httpStatus = httpServletResponse.getStatus();
        String resContent = new String(httpServletResponse.getContentAsByteArray());

        //Todo Response 는 한번 출력하면 복사해서 사용해야한다.
        httpServletResponse.copyBodyToResponse();
        log.info("status: {}, response {}", httpStatus, resContent);
    }
}
