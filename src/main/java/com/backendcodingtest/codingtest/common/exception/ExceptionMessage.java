package com.backendcodingtest.codingtest.common.exception;

import com.backendcodingtest.codingtest.common.exception.httpexception.BadRequestException;
import com.backendcodingtest.codingtest.common.exception.httpexception.ExistException;
import com.backendcodingtest.codingtest.common.exception.httpexception.NotFountException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    DUPLICATE_ITEM_NAME(new ExistException("상품명이 존재합니다. 다시 입력해주세요.", 601)),
    EXIST_RECOMMEND_ITEM(new ExistException("이미 추천 상품이 등록되었습니다. 다시 확인해주세요", 602)),

    NOT_FOUNT_ITEM(new NotFountException("해당 상품이 존재하지 않습니다. 다시 확인해주세요", 701)),
    NOT_FOUNT_RECOMMEND_ITEM(new NotFountException("추천 상품이 존재하지 않습니다. 다시 확인해주세요", 702)),

    BAD_REQUEST_RECOMMEND_ITEM(new BadRequestException("해당 상품의 추천 상품으로 해당 상품을 등록할 수 없습니다.", 801)),
    BAD_REQUEST_DUPLICATE_RECOMMEND_ITEM(new BadRequestException("추천 상품들 중 중복이 존재합니다.", 802));

    private final RuntimeException exception;
}
