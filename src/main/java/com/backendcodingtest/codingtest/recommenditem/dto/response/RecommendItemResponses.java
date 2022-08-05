package com.backendcodingtest.codingtest.recommenditem.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendItemResponses {
    private List<RecommendItemResponse> recommendItemResponseList = new ArrayList<>();

    public RecommendItemResponses(List<RecommendItemResponse> recommendItemResponseList) {
        this.recommendItemResponseList = recommendItemResponseList;
    }

    public static RecommendItemResponses from(List<RecommendItemResponse> recommendItemResponses) {
        return new RecommendItemResponses(
                recommendItemResponses
        );
    }
}
