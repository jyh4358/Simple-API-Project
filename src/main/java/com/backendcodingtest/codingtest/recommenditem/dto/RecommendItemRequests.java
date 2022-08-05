package com.backendcodingtest.codingtest.recommenditem.dto;

import com.backendcodingtest.codingtest.common.validatemessages.ItemRequestMessages;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

import static com.backendcodingtest.codingtest.common.validatemessages.ItemRequestMessages.ITEM_ID_NULL_MESSAGES;
import static com.backendcodingtest.codingtest.common.validatemessages.ItemRequestMessages.RECOMMEND_ITEM_SIZE_MESSAGES;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RecommendItemRequests {
    @NotNull(message = ITEM_ID_NULL_MESSAGES)
    private Long id;
    @Size(max = 20, message = RECOMMEND_ITEM_SIZE_MESSAGES)
    private List<RecommendItemRequest> recommendItemRequestList = new ArrayList<>();
}
