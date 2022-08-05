package com.backendcodingtest.codingtest.item.service;

import com.backendcodingtest.codingtest.common.exception.ExceptionMessage;
import com.backendcodingtest.codingtest.common.exception.httpexception.DuplicateException;
import com.backendcodingtest.codingtest.item.common.ServiceBaseTest;
import com.backendcodingtest.codingtest.item.dto.ItemCreateRequest;
import com.backendcodingtest.codingtest.item.dto.ItemDetail;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponse;
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

    @DisplayName("상품 1개 조회")
    @Test
    public void 상품_조회1() {

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

        String id = String.valueOf(savedItem.getId());

        // when
        ItemDetailResponse itemDetailResponse = itemService.findItem(id);

        // then
        Assertions.assertThat(itemDetailResponse.getItemDetailList()).hasSize(1);

        ItemDetail itemDetail = itemDetailResponse.getItemDetailList().get(0);
        Assertions.assertThat(itemDetail.getId()).isEqualTo(savedItem.getId());
        Assertions.assertThat(itemDetail.getName()).isEqualTo(savedItem.getName());
        Assertions.assertThat(itemDetail.getImageUrl()).isEqualTo(savedItem.getImageUrl());
        Assertions.assertThat(itemDetail.getContentUrl()).isEqualTo(savedItem.getContentUrl());
        Assertions.assertThat(itemDetail.getOriginalPrice()).isEqualTo(savedItem.getOriginalPrice());
        Assertions.assertThat(itemDetail.getSalePrice()).isEqualTo(savedItem.getSalePrice());
    }

    @DisplayName("상품 2개 조회")
    @Test
    public void 상품_조회2() {

        // given
        Item savedItem1 = itemRepository.save(
                new Item(
                        "상품",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );
        Item savedItem2 = itemRepository.save(
                new Item(
                        "상품2",
                        "www.imageUrl2.com",
                        "www.contentUrl2.com",
                        20000,
                        10000
                )
        );

        String id = savedItem1.getId() + "," + savedItem2.getId();

        // when
        ItemDetailResponse itemDetailResponse = itemService.findItem(id);

        // then
        Assertions.assertThat(itemDetailResponse.getItemDetailList()).hasSize(2);

        ItemDetail itemDetail1 = itemDetailResponse.getItemDetailList().get(0);
        Assertions.assertThat(itemDetail1.getId()).isEqualTo(savedItem1.getId());
        Assertions.assertThat(itemDetail1.getName()).isEqualTo(savedItem1.getName());
        Assertions.assertThat(itemDetail1.getImageUrl()).isEqualTo(savedItem1.getImageUrl());
        Assertions.assertThat(itemDetail1.getContentUrl()).isEqualTo(savedItem1.getContentUrl());
        Assertions.assertThat(itemDetail1.getOriginalPrice()).isEqualTo(savedItem1.getOriginalPrice());
        Assertions.assertThat(itemDetail1.getSalePrice()).isEqualTo(savedItem1.getSalePrice());

        ItemDetail itemDetail2 = itemDetailResponse.getItemDetailList().get(1);
        Assertions.assertThat(itemDetail2.getId()).isEqualTo(savedItem2.getId());
        Assertions.assertThat(itemDetail2.getName()).isEqualTo(savedItem2.getName());
        Assertions.assertThat(itemDetail2.getImageUrl()).isEqualTo(savedItem2.getImageUrl());
        Assertions.assertThat(itemDetail2.getContentUrl()).isEqualTo(savedItem2.getContentUrl());
        Assertions.assertThat(itemDetail2.getOriginalPrice()).isEqualTo(savedItem2.getOriginalPrice());
        Assertions.assertThat(itemDetail2.getSalePrice()).isEqualTo(savedItem2.getSalePrice());
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
