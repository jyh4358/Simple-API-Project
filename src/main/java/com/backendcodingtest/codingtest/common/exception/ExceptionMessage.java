package com.backendcodingtest.codingtest.common.exception;

import com.backendcodingtest.codingtest.common.exception.httpexception.DuplicateException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    DUPLICATE_ITEM_NAME(new DuplicateException("상품명이 존재합니다. 다시 입력해주세요.", 601));

    private final RuntimeException exception;
}
