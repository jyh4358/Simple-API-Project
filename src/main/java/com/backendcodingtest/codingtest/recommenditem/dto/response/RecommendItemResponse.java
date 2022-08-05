package com.backendcodingtest.codingtest.recommenditem.dto.response;

import com.backendcodingtest.codingtest.item.dto.ItemDetail;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendItemResponse {
    private ItemDetail target;
    private List<ResultItemResponse> results = new ArrayList<>();

    public RecommendItemResponse(
            ItemDetail target,
            List<ResultItemResponse> results
    ) {
        this.target = target;
        this.results = results;
    }

    public static RecommendItemResponse of(
            ItemDetail target,
            List<ResultItemResponse> results
    ) {
        return new RecommendItemResponse(
                target,
                results
        );
    }
}
