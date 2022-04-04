package com.jp.boiler.base.common.type;

import lombok.Getter;

public enum MDCKey {
    TRX_ID("trxId"),API_CODE("apiCode");

    @Getter
    private String key;

    MDCKey(String key) {
        this.key = key;
    }
}
