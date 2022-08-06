package com.backendcodingtest.codingtest.item.dto;

import com.backendcodingtest.codingtest.item.model.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ItemDetailResponse {
    private Long id;
    private String name;
    private String imageUrl;
    private String contentUrl;
    private int originalPrice;
    private int salePrice;

    public ItemDetailResponse(
            Long id,
            String name,
            String imageUrl,
            String contentUrl,
            int originalPrice,
            int salePrice
    ) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
    }

    public static ItemDetailResponse of(Item item) {
        return new ItemDetailResponse(
                item.getId(),
                item.getName(),
                item.getImageUrl(),
                item.getContentUrl(),
                item.getOriginalPrice(),
                item.getSalePrice()
        );
    }
}
