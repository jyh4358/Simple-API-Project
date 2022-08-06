package com.backendcodingtest.codingtest.common.exception.httpexception;

public class ExistException extends BaseHttpException{
    public ExistException(String message, int code) {
        super(message, code);
    }
}
