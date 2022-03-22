package com.jp.boiler.base.controller.payload;

import lombok.Getter;

@Getter
public class ErrorPayload {

    private final String message;

    private final String description;

    private final String detailMessage;

    private final String requestId;

    public ErrorPayload(String message) {
        this(message, null, null);
    }

    public ErrorPayload(String message, String description) {
        this(message, description, null);
    }

    public ErrorPayload(String message, String description, String detailMessage) {
        this(message, description, detailMessage, null);
    }

    public ErrorPayload(String message, String description, String detailMessage, String requestId) {
        this.message = message;
        this.description = description;
        this.detailMessage = detailMessage;
        this.requestId = requestId;
    }

}
