package com.jp.boiler.base.common.code;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

public enum ResultCode {

    RESULT_0000("0000","성공"),
    RESULT_4000("4000","잘못 된 요청입니다.", HttpStatus.BAD_REQUEST);

    @Getter
    private String resultCode;

    @Getter
    @Setter
    private String resultMessage;

    @Getter
    private HttpStatus httpStatus;

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
