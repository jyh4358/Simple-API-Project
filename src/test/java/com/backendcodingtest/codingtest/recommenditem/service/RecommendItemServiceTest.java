package com.backendcodingtest.codingtest.recommenditem.service;

import com.backendcodingtest.codingtest.common.basetest.ServiceBaseTest;
import com.backendcodingtest.codingtest.common.exception.httpexception.ExistException;
import com.backendcodingtest.codingtest.common.exception.httpexception.NotFountException;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.item.service.ItemService;
import com.backendcodingtest.codingtest.recommenditem.dto.*;
import com.backendcodingtest.codingtest.recommenditem.dto.response.RecommendItemResponse;
import com.backendcodingtest.codingtest.recommenditem.dto.response.RecommendItemResponses;
import com.backendcodingtest.codingtest.recommenditem.dto.response.ResultItemResponse;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class RecommendItemServiceTest extends ServiceBaseTest {

    @Autowired
    private RecommendItemService recommendItemService;

    @Autowired
    private ItemService itemService;

    @AfterEach
    void tearDown() {
        this.databaseCleaner.clean();
    }

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
                recommendItemRequestList
        );

        // when
        recommendItemService.saveRecommendItem(targetItem.getId(), recommendItemRequests);

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

    @DisplayName("[예외]특정 상품의 추천 상품을 재등록 시 예외가 발생한다.")
    @Test
    public void 추천_상품_등록_예외1() {
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
            recommendItemRepository.save(new RecommendItem(targetItem, resultItem, 20 - i));
        }

        RecommendItemRequests recommendItemRequests = new RecommendItemRequests(
                recommendItemRequestList
        );

        // when&&then
        Assertions.assertThatThrownBy(() ->
                        recommendItemService.saveRecommendItem(targetItem.getId(), recommendItemRequests))
                .isInstanceOf(ExistException.class);

    }

    @DisplayName("[예외] 특정 상품이 존재하지 않을 경우 추천 상품을 등록 시 예외가 발생한다.")
    @Test
    public void 추천_상품_등록_예외2() {
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
                recommendItemRequestList
        );

        itemRepository.delete(targetItem);


        // when&then
        Assertions.assertThatThrownBy(() ->
                        recommendItemService.saveRecommendItem(targetItem.getId(), recommendItemRequests))
                .isInstanceOf(NotFountException.class);
    }


    @DisplayName("특정 상품의 추천 상품을 조회한다.")
    @Test
    public void 추천_상품_조회() {
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
        Item resultItem1 = itemRepository.save(
                new Item(
                        "상품2",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );
        Item resultItem2 = itemRepository.save(
                new Item(
                        "상품3",
                        "www.imageUrl2.com",
                        "www.contentUrl2.com",
                        20000,
                        10000
                )
        );
        recommendItemRepository.save(new RecommendItem(targetItem, resultItem1, 20));
        recommendItemRepository.save(new RecommendItem(targetItem, resultItem2, 19));


        // when
        RecommendItemResponses recommendItemResponses = recommendItemService.findRecommendItems(String.valueOf(targetItem.getId()));

        // then
        Assertions.assertThat(recommendItemResponses.getRecommendItemResponseList()).hasSize(1);

        RecommendItemResponse recommendItemResponse = recommendItemResponses.getRecommendItemResponseList().get(0);
        Assertions.assertThat(recommendItemResponse.getTarget().getId()).isEqualTo(targetItem.getId());
        Assertions.assertThat(recommendItemResponse.getTarget().getName()).isEqualTo(targetItem.getName());
        Assertions.assertThat(recommendItemResponse.getTarget().getImageUrl()).isEqualTo(targetItem.getImageUrl());
        Assertions.assertThat(recommendItemResponse.getTarget().getContentUrl()).isEqualTo(targetItem.getContentUrl());
        Assertions.assertThat(recommendItemResponse.getTarget().getOriginalPrice()).isEqualTo(targetItem.getOriginalPrice());
        Assertions.assertThat(recommendItemResponse.getTarget().getSalePrice()).isEqualTo(targetItem.getSalePrice());

        ResultItemResponse resultItemResponse1 = recommendItemResponse.getResults().get(0);
        Assertions.assertThat(resultItemResponse1.getId()).isEqualTo(resultItem1.getId());
        Assertions.assertThat(resultItemResponse1.getName()).isEqualTo(resultItem1.getName());
        Assertions.assertThat(resultItemResponse1.getImageUrl()).isEqualTo(resultItem1.getImageUrl());
        Assertions.assertThat(resultItemResponse1.getContentUrl()).isEqualTo(resultItem1.getContentUrl());
        Assertions.assertThat(resultItemResponse1.getOriginalPrice()).isEqualTo(resultItem1.getOriginalPrice());
        Assertions.assertThat(resultItemResponse1.getSalePrice()).isEqualTo(resultItem1.getSalePrice());
        Assertions.assertThat(resultItemResponse1.getScore()).isEqualTo(20);

        ResultItemResponse resultItemResponse2 = recommendItemResponse.getResults().get(1);
        Assertions.assertThat(resultItemResponse2.getId()).isEqualTo(resultItem2.getId());
        Assertions.assertThat(resultItemResponse2.getName()).isEqualTo(resultItem2.getName());
        Assertions.assertThat(resultItemResponse2.getImageUrl()).isEqualTo(resultItem2.getImageUrl());
        Assertions.assertThat(resultItemResponse2.getContentUrl()).isEqualTo(resultItem2.getContentUrl());
        Assertions.assertThat(resultItemResponse2.getOriginalPrice()).isEqualTo(resultItem2.getOriginalPrice());
        Assertions.assertThat(resultItemResponse2.getSalePrice()).isEqualTo(resultItem2.getSalePrice());
        Assertions.assertThat(resultItemResponse2.getScore()).isEqualTo(19);
    }

    @DisplayName("특정 상품의 추천 상품들을 수정한다.")
    @Test
    public void 추천_상품_수정() {
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
            recommendItemRepository.save(new RecommendItem(targetItem, resultItem, 20 - i));
        }

        List<RecommendItemRequest> recommendItemRequestList = new ArrayList<>();
        List<Item> resultItemList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Item resultItem = itemRepository.save(
                    new Item(
                            "수정 상품" + i,
                            "www.imageUrl" + i + ".com",
                            "www.contentUrl" + i + ".com",
                            10000 * i,
                            5000 * i
                    )
            );
            resultItemList.add(resultItem);
            recommendItemRequestList.add(new RecommendItemRequest(resultItem.getId(), 20 - i));
        }

        RecommendItemRequests recommendItemRequests = new RecommendItemRequests(
                recommendItemRequestList
        );

        // when
        recommendItemService.updateRecommendItem(targetItem.getId(), recommendItemRequests);

        // then
        List<RecommendItem> recommendItemList = recommendItemRepository.findAll();
        Assertions.assertThat(recommendItemList).hasSize(2);

        Assertions.assertThat(recommendItemList.stream()
                        .map(RecommendItem::getResultItem)
                        .map(Item::getId))
                .containsOnly(resultItemList.get(0).getId(), resultItemList.get(1).getId());
    }

    @DisplayName("특정 상품의 추천 상품 삭제한다.")
    @Test
    public void 추천_상품_삭제() {
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
        Item resultItem = itemRepository.save(
                new Item(
                        "상품2",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );
        RecommendItem savedRecommendItemId = recommendItemRepository.save(new RecommendItem(targetItem, resultItem, 20));


        // when
        recommendItemService.deleteRecommendItem(targetItem.getId(), savedRecommendItemId.getId());


        // then
        List<RecommendItem> findRecommendItems = recommendItemRepository.findAll();
        Assertions.assertThat(findRecommendItems).hasSize(0);


    }

    @DisplayName("[예외]특정 상품의 추천 상품 삭제 시 특정 상품이 존재하지 않을 경우 예외가 발생한다.")
    @Test
    public void 추천_상품_삭제_예외1() {
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

        Item resultItem1 = itemRepository.save(
                new Item(
                        "상품2",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );
        Item resultItem2 = itemRepository.save(
                new Item(
                        "상품3",
                        "www.imageUrl2.com",
                        "www.contentUrl2.com",
                        20000,
                        10000
                )
        );
        recommendItemRepository.save(new RecommendItem(targetItem, resultItem1, 20));
        recommendItemRepository.save(new RecommendItem(targetItem, resultItem2, 19));

        itemService.deleteItem(targetItem.getId());


        // when&then
        Assertions.assertThatThrownBy(() ->
                recommendItemService.deleteRecommendItem(targetItem.getId(), resultItem1.getId())
        ).isInstanceOf(NotFountException.class);
    }

    @DisplayName("[예외]특정 상품의 추천 상품 삭제 시 추천 상품이 등록되어 있지 않으면 예외가 발생한다.")
    @Test
    public void 추천_상품_삭제_예외2() {
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
        Item resultItem1 = itemRepository.save(
                new Item(
                        "상품2",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );

        RecommendItem savedRecommendItem = recommendItemRepository.save(new RecommendItem(targetItem, resultItem1, 20));
        recommendItemRepository.delete(savedRecommendItem);

        // when&then
        Assertions.assertThatThrownBy(() ->
                recommendItemService.deleteRecommendItem(targetItem.getId(), savedRecommendItem.getId())
        ).isInstanceOf(NotFountException.class);
    }

}
