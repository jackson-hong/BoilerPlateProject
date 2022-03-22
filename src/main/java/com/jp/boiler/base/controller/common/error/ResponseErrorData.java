package com.jp.boiler.base.controller.common.error;

import com.jp.boiler.base.common.code.ResultCode;
import com.jp.boiler.base.controller.ResponseData;
import com.jp.boiler.base.controller.payload.ErrorPayload;

public class ResponseErrorData extends ResponseData<ErrorPayload> {

    public ResponseErrorData(ResultCode resultCode, ErrorPayload data) {
        super(resultCode, data);
    }

}
