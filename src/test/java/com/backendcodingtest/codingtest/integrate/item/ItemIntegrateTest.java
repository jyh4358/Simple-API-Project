package com.backendcodingtest.codingtest.integrate.item;

import com.backendcodingtest.codingtest.common.basetest.IntegrateBaseTest;
import com.backendcodingtest.codingtest.item.dto.ItemCreateAndUpdateRequest;
import com.backendcodingtest.codingtest.item.model.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class ItemIntegrateTest extends IntegrateBaseTest {

    @AfterEach
    void tearDown() {
        this.databaseCleaner.clean();
    }

    @DisplayName("상품 등록 통합테스트")
    @Test
    public void 상품_등록_통합테스트() throws Exception {
        // given
        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest = new ItemCreateAndUpdateRequest(
                "양발",
                "www.image-url.com",
                "www.content-url.com",
                10000,
                5000
        );

        // when
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(itemCreateAndUpdateRequest)))
                .andExpect(status().isCreated())
                .andDo(print());

        //then
        List<Item> findItemList = itemRepository.findAll();
        Assertions.assertThat(findItemList).hasSize(1);

        Item findItem = findItemList.get(0);
        Assertions.assertThat(findItem.getName()).isEqualTo(itemCreateAndUpdateRequest.getName());
        Assertions.assertThat(findItem.getImageUrl()).isEqualTo(itemCreateAndUpdateRequest.getImageUrl());
        Assertions.assertThat(findItem.getContentUrl()).isEqualTo(itemCreateAndUpdateRequest.getContentUrl());
        Assertions.assertThat(findItem.getOriginalPrice()).isEqualTo(itemCreateAndUpdateRequest.getOriginalPrice());
        Assertions.assertThat(findItem.getSalePrice()).isEqualTo(itemCreateAndUpdateRequest.getSalePrice());
    }

    @DisplayName("상품 정보 조회 통합테스트")
    @Test
    public void 상품_정보_조회_통합테스트() throws Exception {

        // given
        Item savedItem = itemRepository.save(new Item(
                "양발",
                "www.image-url.com",
                "www.content-url.com",
                10000,
                5000
        ));


        // when&then
        mockMvc.perform(get("/items/{id}", savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(savedItem.getId()))
                .andExpect(jsonPath("name").value(savedItem.getName()))
                .andExpect(jsonPath("imageUrl").value(savedItem.getImageUrl()))
                .andExpect(jsonPath("contentUrl").value(savedItem.getContentUrl()))
                .andExpect(jsonPath("originalPrice").value(savedItem.getOriginalPrice()))
                .andExpect(jsonPath("salePrice").value(savedItem.getSalePrice()))
                .andDo(print());
    }

    @DisplayName("상품 수정 통합테스트")
    @Test
    public void 상품_수정_통합테스트() throws Exception {

        // given
        Item savedItem = itemRepository.save(new Item(
                "양발",
                "www.image-url.com",
                "www.content-url.com",
                10000,
                5000
        ));

        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest = new ItemCreateAndUpdateRequest(
                "수정된 양발",
                "www.update-image-url.com",
                "www.update-content-url.com",
                20000,
                10000
        );

        // when
        mockMvc.perform(put("/items/{id}", savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(itemCreateAndUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        List<Item> findItems = itemRepository.findAll();
        Assertions.assertThat(findItems).hasSize(1);

        Item findItem = findItems.get(0);
        Assertions.assertThat(findItem.getId()).isEqualTo(savedItem.getId());
        Assertions.assertThat(findItem.getName()).isEqualTo(itemCreateAndUpdateRequest.getName());
        Assertions.assertThat(findItem.getImageUrl()).isEqualTo(itemCreateAndUpdateRequest.getImageUrl());
        Assertions.assertThat(findItem.getContentUrl()).isEqualTo(itemCreateAndUpdateRequest.getContentUrl());
        Assertions.assertThat(findItem.getOriginalPrice()).isEqualTo(itemCreateAndUpdateRequest.getOriginalPrice());
        Assertions.assertThat(findItem.getSalePrice()).isEqualTo(itemCreateAndUpdateRequest.getSalePrice());
    }

    @DisplayName("상품 삭제 통합테스트")
    @Test
    public void 상품_삭제_통합테스트() throws Exception {

        // given
        Item savedItem = itemRepository.save(new Item(
                "양발",
                "www.image-url.com",
                "www.content-url.com",
                10000,
                5000
        ));


        // when
        mockMvc.perform(delete("/items/{id}", savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        List<Item> findItems = itemRepository.findAll();
        Assertions.assertThat(findItems).hasSize(0);


    }
}
