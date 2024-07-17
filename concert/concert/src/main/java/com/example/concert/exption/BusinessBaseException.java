package com.example.concert.exption;

//Todo RuntimeException을 상속한 개별 Exception 추가
public class BusinessBaseException extends RuntimeException{
    //Todo 에러코드 에러코드 의존성 주입
    private final ErrorCode errorCode;

    //Todo 에러코드와 message 가 같이 들어올 경우
    public BusinessBaseException(ErrorCode errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }

    //Todo 에러코드 의존성 주입
    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    //Todo  ErrorCode
    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
