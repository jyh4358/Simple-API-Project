package com.backendcodingtest.codingtest.item.service;

import com.backendcodingtest.codingtest.common.basetest.ServiceBaseTest;
import com.backendcodingtest.codingtest.common.exception.httpexception.ExistException;
import com.backendcodingtest.codingtest.common.exception.httpexception.NotFountException;
import com.backendcodingtest.codingtest.item.dto.ItemCreateAndUpdateRequest;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponse;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponses;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class ItemServiceTest extends ServiceBaseTest {

    @Autowired
    private ItemService itemService;

    @AfterEach
    void tearDown() {
        this.databaseCleaner.clean();
    }

    @DisplayName("상품을 등록한다.")
    @Test
    public void 상품_등록() {

        // given
        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest = new ItemCreateAndUpdateRequest(
                "상품1",
                "www.imageUrl.com",
                "www.contentUrl.com",
                10000,
                5000
        );

        // when
        itemService.saveItem(itemCreateAndUpdateRequest);

        // then
        List<Item> findItemList = itemRepository.findAll();
        Assertions.assertThat(findItemList).hasSize(1);

        Item findItem = findItemList.get(0);
        Assertions.assertThat(findItem.getName()).isEqualTo(itemCreateAndUpdateRequest.getName());
        Assertions.assertThat(findItem.getImageUrl()).isEqualTo(itemCreateAndUpdateRequest.getImageUrl());
        Assertions.assertThat(findItem.getContentUrl()).isEqualTo(itemCreateAndUpdateRequest.getContentUrl());
        Assertions.assertThat(findItem.getOriginalPrice()).isEqualTo(itemCreateAndUpdateRequest.getOriginalPrice());
        Assertions.assertThat(findItem.getSalePrice()).isEqualTo(itemCreateAndUpdateRequest.getSalePrice());
    }

    @DisplayName("[예외] 중복된 상품이름 등록 시 예외가 발생한다.")
    @Test
    public void 상품_등록_예외() {

        // given
        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest1 = new ItemCreateAndUpdateRequest(
                "상품",
                "www.imageUrl.com",
                "www.contentUrl.com",
                10000,
                5000
        );
        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest2 = new ItemCreateAndUpdateRequest(
                "상품",
                "www.imageUrl.com",
                "www.contentUrl.com",
                10000,
                5000
        );

        itemService.saveItem(itemCreateAndUpdateRequest1);


        // when&then
        Assertions.assertThatThrownBy(() ->
                        itemService.saveItem(itemCreateAndUpdateRequest2))
                .isInstanceOf(ExistException.class);

    }

    @DisplayName("상품 1개을 조회 한다.")
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


        // when
        ItemDetailResponse itemDetailResponse = itemService.findItem(savedItem.getId());

        // then
        Assertions.assertThat(itemDetailResponse.getId()).isEqualTo(savedItem.getId());
        Assertions.assertThat(itemDetailResponse.getName()).isEqualTo(savedItem.getName());
        Assertions.assertThat(itemDetailResponse.getImageUrl()).isEqualTo(savedItem.getImageUrl());
        Assertions.assertThat(itemDetailResponse.getContentUrl()).isEqualTo(savedItem.getContentUrl());
        Assertions.assertThat(itemDetailResponse.getOriginalPrice()).isEqualTo(savedItem.getOriginalPrice());
        Assertions.assertThat(itemDetailResponse.getSalePrice()).isEqualTo(savedItem.getSalePrice());
    }

    @DisplayName("전체 상품을 조회한다.")
    @Test
    public void 전체_상품_조회2() {

        // given
        itemRepository.save(
                new Item(
                        "상품",
                        "www.imageUrl.com",
                        "www.contentUrl.com",
                        10000,
                        5000
                )
        );
        itemRepository.save(
                new Item(
                        "상품2",
                        "www.imageUrl2.com",
                        "www.contentUrl2.com",
                        20000,
                        10000
                )
        );


        // when
        ItemDetailResponses itemDetailResponses = itemService.findAllItem();

        // then
        Assertions.assertThat(itemDetailResponses.getItemDetailResponseList()).hasSize(2);
    }

    @DisplayName("상품을 수정한다.")
    @Test
    public void 상품_수정() {

        // given
        Item savedItem = itemRepository.save(
                new Item(
                        "상품",
                        "www.image-url.com",
                        "www.content-url.com",
                        10000,
                        5000
                )
        );

        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest = new ItemCreateAndUpdateRequest(
                "수정된 상품",
                "www.update-image-url.com",
                "www.update-content-url.com",
                20000,
                10000
        );


        // when
        itemService.updateItem(savedItem.getId(), itemCreateAndUpdateRequest);

        // then
        Item findItem = itemRepository.findById(savedItem.getId()).get();

        Assertions.assertThat(findItem.getName()).isEqualTo(itemCreateAndUpdateRequest.getName());
        Assertions.assertThat(findItem.getImageUrl()).isEqualTo(itemCreateAndUpdateRequest.getImageUrl());
        Assertions.assertThat(findItem.getContentUrl()).isEqualTo(itemCreateAndUpdateRequest.getContentUrl());
        Assertions.assertThat(findItem.getOriginalPrice()).isEqualTo(itemCreateAndUpdateRequest.getOriginalPrice());
        Assertions.assertThat(findItem.getSalePrice()).isEqualTo(itemCreateAndUpdateRequest.getSalePrice());
    }

    @DisplayName("[예외]상품 수정 시 상품이 존재하지 않으면 예외가 발생한다.")
    @Test
    public void 상품_수정_예외1() {

        // given
        Item savedItem = itemRepository.save(
                new Item(
                        "상품",
                        "www.image-url.com",
                        "www.content-url.com",
                        10000,
                        5000
                )
        );

        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest = new ItemCreateAndUpdateRequest(
                "수정된 상품",
                "www.update-image-url.com",
                "www.update-content-url.com",
                20000,
                10000
        );

        // when
        itemRepository.delete(savedItem);

        // then
        Assertions.assertThatThrownBy(() ->
                itemService.updateItem(savedItem.getId(), itemCreateAndUpdateRequest))
                .isInstanceOf(NotFountException.class);
    }

    @DisplayName("[예외] 상품 수정 시 중복된 상품이름으로 요청하면 예외가 발생한다.")
    @Test
    public void 상품_수정_예외2() {

        // given
        Item savedItem = itemRepository.save(
                new Item(
                        "상품",
                        "www.image-url.com",
                        "www.content-url.com",
                        10000,
                        5000
                )
        );

        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest = new ItemCreateAndUpdateRequest(
                "상품",
                "www.update-image-url.com",
                "www.update-content-url.com",
                20000,
                10000
        );

        // when&then
        Assertions.assertThatThrownBy(() ->
                        itemService.updateItem(savedItem.getId(), itemCreateAndUpdateRequest))
                .isInstanceOf(ExistException.class);

    }

    @DisplayName("상품을 삭제한다.")
    @Test
    public void 상품_삭제() {
        // given
        Item savedItem = itemRepository.save(
                new Item(
                        "상품",
                        "www.image-url.com",
                        "www.content-url.com",
                        10000,
                        5000
                )
        );

        // when
        itemService.deleteItem(savedItem.getId());

        // then
        List<Item> findAllItem = itemRepository.findAll();
        Assertions.assertThat(findAllItem).hasSize(0);
    }

    @DisplayName("[예외] 존재하지 않는 상품을 삭제 시 예외가 발생한다.")
    @Test
    public void 상품_삭제_예외() {
        // given
        Item savedItem = itemRepository.save(
                new Item(
                        "상품",
                        "www.image-url.com",
                        "www.content-url.com",
                        10000,
                        5000
                )
        );

        itemService.deleteItem(savedItem.getId());

        // when*then
        Assertions.assertThatThrownBy(() ->
                        itemService.deleteItem(savedItem.getId()))
                .isInstanceOf(NotFountException.class);

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



        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest = new ItemCreateAndUpdateRequest(
                "상품",
                "www.imageUrl.com",
                "www.contentUrl.com",
                100000,
                50000
        );

        // when
        itemService.saveItem(itemCreateAndUpdateRequest);

        List<Item> findItemList = itemRepository.findAll();
        Item findItem = findItemList.get(findItemList.size() - 1);


        // then
        Assertions.assertThat(findItemList).hasSize(2);
        Assertions.assertThat(findItem.getName()).isEqualTo(itemCreateAndUpdateRequest.getName());
        Assertions.assertThat(findItem.getImageUrl()).isEqualTo(itemCreateAndUpdateRequest.getImageUrl());
        Assertions.assertThat(findItem.getContentUrl()).isEqualTo(itemCreateAndUpdateRequest.getContentUrl());
        Assertions.assertThat(findItem.getOriginalPrice()).isEqualTo(itemCreateAndUpdateRequest.getOriginalPrice());
        Assertions.assertThat(findItem.getSalePrice()).isEqualTo(itemCreateAndUpdateRequest.getSalePrice());
        Assertions.assertThat(findItem.getTargetItemList()).hasSize(1);
        Assertions.assertThat(findItem.getResultItemList()).hasSize(1);
        Assertions.assertThat(findItem.getTargetItemList().get(0).getScore()).isEqualTo(20);
    }

}
