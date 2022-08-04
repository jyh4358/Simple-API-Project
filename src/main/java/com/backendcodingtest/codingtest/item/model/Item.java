package com.backendcodingtest.codingtest.item.model;

import com.backendcodingtest.codingtest.common.model.BasicEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BasicEntity {

    private String name;
    private String imageUrl;
    private String contentUrl;
    private int originalPrice;
    private int salePrice;

    @OneToMany(mappedBy = "targetItem")
    private List<RecommendItem> targetItemList = new ArrayList<>();

    @OneToMany(mappedBy = "resultItem")
    private List<RecommendItem> resultItemList = new ArrayList<>();

    @Builder
    public Item(
            String name,
            String imageUrl,
            String contentUrl,
            int originalPrice,
            int salePrice
    ) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.contentUrl = contentUrl;
        this.originalPrice = originalPrice;
        this.salePrice = salePrice;
    }
}
