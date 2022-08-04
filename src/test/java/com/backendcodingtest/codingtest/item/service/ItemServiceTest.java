package com.backendcodingtest.codingtest.item.service;

import com.backendcodingtest.codingtest.common.exception.ExceptionMessage;
import com.backendcodingtest.codingtest.common.exception.httpexception.DuplicateException;
import com.backendcodingtest.codingtest.item.common.ServiceBaseTest;
import com.backendcodingtest.codingtest.item.dto.ItemCreateRequest;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItemRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class ItemServiceTest extends ServiceBaseTest {

    @Autowired
    private ItemService itemService;

    @DisplayName("상품을 등록한다.")
    @Test
    public void 상품_등록() {

        // given
        ItemCreateRequest itemCreateRequest = new ItemCreateRequest(
                "상품1",
                "www.imageUrl.com",
                "www.contentUrl.com",
                10000,
                5000
        );

        // when
        itemService.saveItem(itemCreateRequest);

        // then
        List<Item> findItemList = itemRepository.findAll();
        Assertions.assertThat(findItemList).hasSize(1);

        Item findItem = findItemList.get(0);
        Assertions.assertThat(findItem.getName()).isEqualTo(itemCreateRequest.getName());
        Assertions.assertThat(findItem.getImageUrl()).isEqualTo(itemCreateRequest.getImageUrl());
        Assertions.assertThat(findItem.getContentUrl()).isEqualTo(itemCreateRequest.getContentUrl());
        Assertions.assertThat(findItem.getOriginalPrice()).isEqualTo(itemCreateRequest.getOriginalPrice());
        Assertions.assertThat(findItem.getSalePrice()).isEqualTo(itemCreateRequest.getSalePrice());
    }

    @DisplayName("[예외] 중복된 상품이름 등록 시 예외가 발생한다.")
    @Test
    public void 상품_등록_예외() {

        // given
        ItemCreateRequest itemCreateRequest1 = new ItemCreateRequest(
                "상품",
                "www.imageUrl.com",
                "www.contentUrl.com",
                10000,
                5000
        );
        ItemCreateRequest itemCreateRequest2 = new ItemCreateRequest(
                "상품",
                "www.imageUrl.com",
                "www.contentUrl.com",
                10000,
                5000
        );

        itemService.saveItem(itemCreateRequest1);


        // when&then
        Assertions.assertThatThrownBy(() ->
                        itemService.saveItem(itemCreateRequest2))
                .isInstanceOf(DuplicateException.class);

    }


    @DisplayName("상품 등록하면서 추천 상품도 같이 등록한다.")
//    @Test
    public void 추천_상품과_상품_등록() {

        // given

        Item savedItem = itemRepository.save(
                new Item(
                        "상품",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );


        List<RecommendItemRequest> recommendItemRequestList = Arrays.asList(new RecommendItemRequest(savedItem.getId(), 20));



        ItemCreateRequest itemCreateRequest = new ItemCreateRequest(
                "상품",
                "www.imageUrl.com",
                "www.contentUrl.com",
                100000,
                50000
        );

        // when
        itemService.saveItem(itemCreateRequest);

        List<Item> findItemList = itemRepository.findAll();
        Item findItem = findItemList.get(findItemList.size() - 1);


        // then
        Assertions.assertThat(findItemList).hasSize(2);
        Assertions.assertThat(findItem.getName()).isEqualTo(itemCreateRequest.getName());
        Assertions.assertThat(findItem.getImageUrl()).isEqualTo(itemCreateRequest.getImageUrl());
        Assertions.assertThat(findItem.getContentUrl()).isEqualTo(itemCreateRequest.getContentUrl());
        Assertions.assertThat(findItem.getOriginalPrice()).isEqualTo(itemCreateRequest.getOriginalPrice());
        Assertions.assertThat(findItem.getSalePrice()).isEqualTo(itemCreateRequest.getSalePrice());
        Assertions.assertThat(findItem.getTargetItemList()).hasSize(1);
        Assertions.assertThat(findItem.getResultItemList()).hasSize(1);
        Assertions.assertThat(findItem.getTargetItemList().get(0).getScore()).isEqualTo(20);

    }

}
