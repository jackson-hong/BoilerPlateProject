package com.jp.boiler.base.common.exception;


import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.controller.ResponseData;
import com.jp.boiler.base.controller.common.error.ResponseErrorData;
import com.jp.boiler.base.controller.payload.ErrorPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseData<ErrorPayload>> unhandledException(Exception e) {
        log.error("unhandled", e);
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(ResponseData.error(new ErrorPayload("Unhandled Exception", e.getClass().getSimpleName(), e.getMessage())));
    }

    @ExceptionHandler(BoilerException.class)
    public ResponseEntity<ResponseErrorData> boilerException(BoilerException e) {
        log.error("BoilerException: ", e);
        final ResultCode resultCode = e.getResultCode();
        return ResponseEntity
                .status(resultCode.getHttpStatus())
                .body(new ResponseErrorData(resultCode, new ErrorPayload(resultCode.getResultMessage(), e.getDescription(), e.getStackTrace()[0].toString(), "temp")));
    }
}
