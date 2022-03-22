package com.jp.boiler.base.common.exception;


import com.jp.boiler.base.controller.ResponseData;
import com.jp.boiler.base.controller.payload.ErrorPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseData<ErrorPayload>>
}
