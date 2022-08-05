package com.backendcodingtest.codingtest.recommenditem.model;

import com.backendcodingtest.codingtest.common.model.BasicEntity;
import com.backendcodingtest.codingtest.item.model.Item;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendItem extends BasicEntity {

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "target_item_id")
    private Item targetItem;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "result_item_id")
    private Item resultItem;

    private int score;

    public static RecommendItem creatRecommendItem(
            Item targetItem,
            Item resultItem,
            int score
    ) {
        return new RecommendItem(
                targetItem,
                resultItem,
                score
        );
    }

}
