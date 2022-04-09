package com.jp.boiler.base.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.controller.payload.ErrorPayload;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import static com.jp.boiler.base.common.code.ResultCode.RESULT_0000;
import static com.jp.boiler.base.common.code.ResultCode.RESULT_9999;

@Schema(description = "Response Data")
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseData<T> extends ResponseBase {
    public ResponseData(ResultCode code) {
        this(code, null);
    }

    public ResponseData(ResultCode code, T resultData) {
        super(code);
        this.resultData = resultData;
    }

    private final T resultData;

    public static <T> ResponseData<T> success(T data) {
        return new ResponseData<T>(RESULT_0000, data);
    }

    public static ResponseData<String> success() {
        return new ResponseData<>(RESULT_0000);
    }

    public static ResponseData<String> error() {
        return new ResponseData<>(RESULT_9999);
    }

    public static ResponseData<String> customError(ResultCode resultCode) {
        return new ResponseData<>(resultCode);
    }

    public static ResponseData<String> success(String message) {
        return new ResponseData<>(RESULT_0000, message);
    }

    public static ResponseData<ErrorPayload> error(ErrorPayload payload) {
        return new ResponseData<>(RESULT_9999, payload);
    }
}
