package com.backendcodingtest.codingtest.item.dto;

import com.backendcodingtest.codingtest.item.model.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static com.backendcodingtest.codingtest.common.validatemessages.ItemRequestMessages.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemCreateAndUpdateRequest {
    @NotBlank(message = ITEM_NAME_BLANK_MESSAGES)
    private String name;
    @NotBlank(message = ITEM_IMAGE_URL_BLANK_MESSAGES)
    private String imageUrl;
    @NotBlank(message = ITEM_CONTENT_URL_BLANK_MESSAGES)
    private String contentUrl;
    @NotNull(message = ITEM_ORIGINAL_PRICE_NULL_MESSAGES)
    private int originalPrice;
    @NotNull(message = ITEM_SALE_PRICE_NULL_MESSAGES)
    private int salePrice;

    public Item toEntity() {
        return Item.builder()
                .name(name)
                .imageUrl(imageUrl)
                .contentUrl(contentUrl)
                .originalPrice(originalPrice)
                .salePrice(salePrice)
                .build();
    }
}
