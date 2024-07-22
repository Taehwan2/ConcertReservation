package com.example.concert.exption;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//Todo ResponseEntity 에 담을 ErrorResponse BusinessBaseException 을 통해 받을 Errorcode 로 message 와 code 를 채운다.
public class ErrorResponse {
    // 출력할 message
    private String message;
    // 출력할 code
    private String code;


    //생성자 errorcode 만 들어올 경우
    private ErrorResponse(final ErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    //생성자 errorcode와 message가 들어올경우
    public ErrorResponse(final ErrorCode errorCode,String message){
        this.code = errorCode.getCode();
        this.message = message;
    }
    //of 메서드로 만든다. 1번 생성자
    public static ErrorResponse of(final ErrorCode errorCode){
        return new ErrorResponse(errorCode);
    }

    //of 메서드로 만든다 2번 생성자
    public static ErrorResponse of(final ErrorCode errorCode , String message){
        return new ErrorResponse(errorCode,message);
    }
}
