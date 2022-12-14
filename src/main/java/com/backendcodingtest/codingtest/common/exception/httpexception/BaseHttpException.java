package com.backendcodingtest.codingtest.common.exception.httpexception;

import lombok.Getter;

@Getter
public class BaseHttpException extends RuntimeException{

    private int code;

    public BaseHttpException(String message, int code) {
        super(message);
        this.code = code;
    }
}
