package com.jp.boiler.base.common.code;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

public enum ResultCode {

    RESULT_0000("0000","성공"),
    RESULT_9999("9999","실패"),
    // 토큰 인증오류
    RESULT_4000("4000","잘못 된 요청입니다.", HttpStatus.BAD_REQUEST),
    RESULT_4001("4001", "인증 시간이 만료되었습니다.")
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
