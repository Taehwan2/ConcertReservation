package com.example.concert.exption;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice

//예외 처리를 잡을 Handler
public class GlobalExceptionHandler {

    //Http method가 연결이 안되었을 때 타는 Exception
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class) // HttpRequestMethodNotSupportedException 예외를 잡아서 처리하는 코드
    protected ResponseEntity<ErrorResponse> handle(HttpRequestMethodNotSupportedException e) {

        //error 잡는 로그
        log.error("HttpRequestMethodNotSupportedException", e);
        return createErrorResponseEntity(ErrorCode.METHOD_NOT_ALLOWED);
    }
    //실제 비지니스 로직이 에러가 났을 때 잡는 메서드
    @ExceptionHandler(BusinessBaseException.class)
    protected ResponseEntity<ErrorResponse> handle(BusinessBaseException e) {

        //error 잡는 로그
        log.error("BusinessException", e);
        return createErrorResponseEntity(e.getErrorCode());
    }

    //Server error 가 났을 때 잡는 메서드
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handle(Exception e) {

        //error 잡는 로그
        e.printStackTrace();
        log.error("Exception", e);
        return createErrorResponseEntity(ErrorCode.INTERNAL_SERVER_ERROR);
    }

    //error code 를 통해서 ResponseEntity를 만드는 코드
    private ResponseEntity<ErrorResponse> createErrorResponseEntity(ErrorCode errorCode) {
        return new ResponseEntity<>(
                ErrorResponse.of(errorCode),
                errorCode.getStatus());
    }
}
