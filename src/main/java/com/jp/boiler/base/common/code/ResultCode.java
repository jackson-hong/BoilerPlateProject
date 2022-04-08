package com.jp.boiler.base.common.code;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

public enum ResultCode {

    RESULT_0000("0000","성공"),
    RESULT_9999("9999","실패"),

    RESULT_2000("2000","이미 존재하는 회원입니다."),
    // 토큰 인증오류
    RESULT_4000("4000","잘못 된 요청입니다.", HttpStatus.BAD_REQUEST),
    RESULT_4001("4001", "인증 시간이 만료되었습니다."),


    // 회원 검증오류
    RESULT_1001("1001","비밀번호는 영어와 숫자 특수문자를 포함 8~20 자리 여야 합니다."),
    RESULT_1002("1002","비밀번호는 동일한 문자를 연속으로 사용할 수 없습니다."),
    RESULT_1003("1003","비밀번호에 공백문자를 포함할 수 없습니다.")

;


    @Getter
    private final String resultCode;

    @Getter
    private final String resultMessage;

    @Getter
    private final HttpStatus httpStatus;

    ResultCode(String resultCode, String resultMessage) {
        this(resultCode, resultMessage, HttpStatus.OK);
    }

    ResultCode(String resultCode, String resultMessage, HttpStatus httpStatus) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
        this.httpStatus = httpStatus;
    }

    public static boolean isSuccess(String resultCode) {
        return RESULT_0000.resultCode.equals(resultCode);
    }
}
