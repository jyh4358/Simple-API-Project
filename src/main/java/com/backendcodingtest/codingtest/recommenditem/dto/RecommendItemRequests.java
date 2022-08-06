package com.backendcodingtest.codingtest.recommenditem.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.backendcodingtest.codingtest.common.validatemessages.ItemRequestMessages.RECOMMEND_ITEM_SIZE_MESSAGES;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecommendItemRequests {
    @Size(max = 20, message = RECOMMEND_ITEM_SIZE_MESSAGES)
    private List<RecommendItemRequest> recommendItemRequestList = new ArrayList<>();
}
