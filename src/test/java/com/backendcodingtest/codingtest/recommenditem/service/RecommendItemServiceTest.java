package com.backendcodingtest.codingtest.recommenditem.service;

import com.backendcodingtest.codingtest.common.ServiceBaseTest;
import com.backendcodingtest.codingtest.common.exception.httpexception.NotFountException;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequest;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequests;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RecommendItemServiceTest extends ServiceBaseTest {

    @Autowired
    private RecommendItemService recommendItemService;

    @DisplayName("특정 상품의 추천 상품을 등록한다.")
    @Test
    public void 추천_상품_등록() {
        // given
        Item targetItem = itemRepository.save(
                new Item(
                        "상품",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );

        List<RecommendItemRequest> recommendItemRequestList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Item resultItem = itemRepository.save(
                    new Item(
                            "상품" + i,
                            "www.imageUrl" + i + ".com",
                            "www.contentUrl" + i + ".com",
                            10000 * i,
                            5000 * i
                    )
            );
            recommendItemRequestList.add(new RecommendItemRequest(resultItem.getId(), 20 - i));
        }

        RecommendItemRequests recommendItemRequests = new RecommendItemRequests(
                targetItem.getId(),
                recommendItemRequestList
        );

        // when
        recommendItemService.saveRecommendItem(recommendItemRequests);

        // then
        List<RecommendItem> recommendItemList = recommendItemRepository.findAll();
        Assertions.assertThat(recommendItemList).hasSize(2);

        RecommendItem recommendItem1 = recommendItemList.get(0);
        Assertions.assertThat(recommendItem1.getTargetItem().getId()).isEqualTo(targetItem.getId());
        Assertions.assertThat(recommendItem1.getResultItem().getId())
                .isEqualTo(recommendItemRequests.getRecommendItemRequestList().get(0).getId());
        Assertions.assertThat(recommendItem1.getScore())
                .isEqualTo(recommendItemRequests.getRecommendItemRequestList().get(0).getScore());
    }

    @DisplayName("[예외] 특정 상품이 존재하지 않을 경우 추천 상품을 등록 시 예외가 발생한다.")
    @Test
    public void 추천_상품_등록_예외() {
        // given
        Item targetItem = itemRepository.save(
                new Item(
                        "상품",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );

        List<RecommendItemRequest> recommendItemRequestList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Item resultItem = itemRepository.save(
                    new Item(
                            "상품" + i,
                            "www.imageUrl" + i + ".com",
                            "www.contentUrl" + i + ".com",
                            10000 * i,
                            5000 * i
                    )
            );
            recommendItemRequestList.add(new RecommendItemRequest(resultItem.getId(), 20 - i));
        }

        RecommendItemRequests recommendItemRequests = new RecommendItemRequests(
                targetItem.getId(),
                recommendItemRequestList
        );

        itemRepository.delete(targetItem);


        // when&then
        Assertions.assertThatThrownBy(() ->
                        recommendItemService.saveRecommendItem(recommendItemRequests))
                .isInstanceOf(NotFountException.class);
    }


}
