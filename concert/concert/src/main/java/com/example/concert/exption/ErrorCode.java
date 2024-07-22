package com.example.concert.exption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //TODO NO METHODE 에러코드 모음
     METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED,"E0","헤당 메서드가 선언되지 않았습니다 Method." ),

    //TODO 500 SERVER ERROR 에러코드 모음
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"E1","Server Error."),

    //TODO USER 에러코드 모음
    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"U0","유저를 찾을 수 없습니다.."),
    USER_AMOUNT_CANNOT_BE_ZERO(HttpStatus.BAD_REQUEST, "A0", "금액은 0일 수 없습니다.."),
    USER_INSUFFICIENT_BALANCE(HttpStatus.BAD_REQUEST, "B0", "잔고가 부족합니다.."),

    //TODO QUEUE 에러코드 모음
    QUEUE_ALREADY_WAITING(HttpStatus.CONFLICT, "Q1", "이미 대기 중입니다.."),
    QUEUE_ALREADY_WORKING(HttpStatus.CONFLICT, "Q2", "이미 작업 중입니다.."),
    QUEUE_NOT_FOUND(HttpStatus.NOT_FOUND,"Q3","대기열을 찾을 수 없습니다.."),
    QUEUE_NOT_WORKING(HttpStatus.NOT_FOUND,"Q4","현재 대기열을 통과하지 못했습니다.."),

    //TODO SEAT 에러코드 모음
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND,"S0","좌석을 찾을 수 없습니다.."),
    SEAT_NO_INVALID(HttpStatus.BAD_REQUEST,"S1","잘못된 좌석번호입니다.."),

    //TODO CONCERT 에러코드 모음
    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND,"C0","콘서트가 없습니다.."),

    //TODO CONCERT_DETAIL 에러코드 모음
    CONCERT_DETAIL_NOT_FOUND(HttpStatus.NOT_FOUND,"C1","콘서트 세부정보를 찾아볼 수 없습니다..");



    private final HttpStatus status;

    private final String code;
    private final String message;
}
