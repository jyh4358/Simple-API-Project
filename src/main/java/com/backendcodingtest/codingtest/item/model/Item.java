package com.backendcodingtest.codingtest.item.model;

import com.backendcodingtest.codingtest.common.model.BasicEntity;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BasicEntity {

    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false, length = 100)
    private String imageUrl;
    @Column(nullable = false, length = 100)
    private String contentUrl;
    @Column(nullable = false)
    private int originalPrice;
    private int salePrice;

    @OneToMany(mappedBy = "targetItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecommendItem> targetItemList = new ArrayList<>();

    @OneToMany(mappedBy = "resultItem", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void updateItem(
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
