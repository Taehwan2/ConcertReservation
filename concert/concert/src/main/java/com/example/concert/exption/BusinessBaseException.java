package com.example.concert.exption;

public class BusinessBaseException extends RuntimeException{

    private final ErrorCode errorCode;

    public BusinessBaseException(ErrorCode errorCode,String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BusinessBaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode(){
        return errorCode;
    }
}
