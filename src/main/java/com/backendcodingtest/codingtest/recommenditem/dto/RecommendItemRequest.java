package com.backendcodingtest.codingtest.recommenditem.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

import static com.backendcodingtest.codingtest.common.validatemessages.ItemRequestMessages.ITEM_ID_NULL_MESSAGES;
import static com.backendcodingtest.codingtest.common.validatemessages.ItemRequestMessages.RECOMMEND_ITEM_SCORE_MESSAGES;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecommendItemRequest {
    @NotNull(message = ITEM_ID_NULL_MESSAGES)
    private Long id;
    @NotNull(message = RECOMMEND_ITEM_SCORE_MESSAGES)
    private int score;
}
