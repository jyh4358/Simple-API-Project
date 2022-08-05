package com.backendcodingtest.codingtest.common.exception.httpexception;

public class NotFountException extends BaseHttpException{
    public NotFountException(String message, int code) {
        super(message, code);
    }
}
