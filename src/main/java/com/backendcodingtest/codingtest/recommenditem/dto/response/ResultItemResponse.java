package com.backendcodingtest.codingtest.recommenditem.dto.response;

import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResultItemResponse {

    private Long id;
    private String name;
    private String imageUrl;
    private String contentUrl;
    private int originalPrice;
    private int salePrice;
    private Long recommendId;
    private int score;
    private int rank;

    public static ResultItemResponse of(
            Item result,
            RecommendItem recommendItem,
            int rank
    ) {
        return new ResultItemResponse(
                result.getId(),
                result.getName(),
                result.getImageUrl(),
                result.getContentUrl(),
                result.getOriginalPrice(),
                result.getSalePrice(),
                recommendItem.getId(),
                recommendItem.getScore(),
                rank
        );
    }

}
