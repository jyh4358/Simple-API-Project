package com.backendcodingtest.codingtest.common.exception.httpexception;

public class DuplicateException extends BaseHttpException{
    public DuplicateException(String message, int code) {
        super(message, code);
    }
}
