package com.backendcodingtest.codingtest.recommenditem.dto.response;

import com.backendcodingtest.codingtest.item.dto.ItemDetailResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendItemResponse {
    private ItemDetailResponse target;
    private List<ResultItemResponse> results = new ArrayList<>();

    public RecommendItemResponse(
            ItemDetailResponse target,
            List<ResultItemResponse> results
    ) {
        this.target = target;
        this.results = results;
    }

    public static RecommendItemResponse of(
            ItemDetailResponse target,
            List<ResultItemResponse> results
    ) {
        return new RecommendItemResponse(
                target,
                results
        );
    }
}
