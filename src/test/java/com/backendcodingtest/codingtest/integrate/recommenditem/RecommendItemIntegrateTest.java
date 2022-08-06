package com.backendcodingtest.codingtest.integrate.recommenditem;

import com.backendcodingtest.codingtest.common.basetest.IntegrateBaseTest;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequest;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequests;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecommendItemIntegrateTest extends IntegrateBaseTest {

    @AfterEach
    void tearDown() {
        this.databaseCleaner.clean();
    }

    @DisplayName("추천 상품 등록 통합테스트")
    @Test
    public void  추천_상품_등록_통합테스트() throws Exception {

        // given
        Item savedTargetItem = itemRepository.save(new Item(
                "신발",
                "www.image-url1.com",
                "www.content-url1.com",
                30000,
                20000
        ));
        Item savedResultItem1 = itemRepository.save(new Item(
                "양발1",
                "www.image-url2.com",
                "www.content-url2.com",
                20000,
                15000
        ));
        Item savedResultItem2 = itemRepository.save(new Item(
                "양발2",
                "www.image-url3.com",
                "www.content-url3.com",
                10000,
                5000
        ));

        List<RecommendItemRequest> recommendItemRequestList = Arrays.asList(
                new RecommendItemRequest(savedResultItem1.getId(), 20),
                new RecommendItemRequest(savedResultItem2.getId(), 19)
        );

        RecommendItemRequests recommendItemRequests = new RecommendItemRequests(
                recommendItemRequestList
        );

        // when
        mockMvc.perform(post("/target-items/{id}/recommend-items", savedTargetItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(recommendItemRequests)))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        List<RecommendItem> findRecommendItem = recommendItemRepository.findAll();
        Assertions.assertThat(findRecommendItem).hasSize(2);

        List<Long> findResultItemId = findRecommendItem.stream()
                .map(RecommendItem::getResultItem)
                .map(Item::getId)
                .collect(Collectors.toList());
        Assertions.assertThat(findResultItemId).containsOnly(savedResultItem1.getId(), savedResultItem2.getId());
    }

    @DisplayName("상품과 추천 상품 조회 통합테스트")
    @Test
    public void  상품과_추천_상품_조회_통합테스트() throws Exception {

        // given
        Item savedTargetItem = itemRepository.save(new Item(
                "신발",
                "www.image-url1.com",
                "www.content-url1.com",
                30000,
                20000
        ));
        Item savedResultItem1 = itemRepository.save(new Item(
                "양발1",
                "www.image-url2.com",
                "www.content-url2.com",
                20000,
                15000
        ));
        Item savedResultItem2 = itemRepository.save(new Item(
                "양발2",
                "www.image-url3.com",
                "www.content-url3.com",
                10000,
                5000
        ));

        RecommendItem savedRecommendItem1 = recommendItemRepository.save(
                new RecommendItem(savedTargetItem, savedResultItem1, 20)
        );
        RecommendItem savedRecommendItem2 = recommendItemRepository.save(
                new RecommendItem(savedTargetItem, savedResultItem2, 19)
        );

        // when
        mockMvc.perform(get("/target-items/recommend-items?id=" + savedTargetItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("recommendItemResponseList", hasSize(1)))
                .andExpect(jsonPath("recommendItemResponseList.[0].target.id").value(savedTargetItem.getId()))
                .andExpect(jsonPath("recommendItemResponseList.[0].target.name").value(savedTargetItem.getName()))
                .andExpect(jsonPath("recommendItemResponseList.[0].target.imageUrl").value(savedTargetItem.getImageUrl()))
                .andExpect(jsonPath("recommendItemResponseList.[0].target.contentUrl").value(savedTargetItem.getContentUrl()))
                .andExpect(jsonPath("recommendItemResponseList.[0].target.originalPrice").value(savedTargetItem.getOriginalPrice()))
                .andExpect(jsonPath("recommendItemResponseList.[0].target.salePrice").value(savedTargetItem.getSalePrice()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results", hasSize(2)))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].id").value(savedResultItem1.getId()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].name").value(savedResultItem1.getName()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].imageUrl").value(savedResultItem1.getImageUrl()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].contentUrl").value(savedResultItem1.getContentUrl()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].originalPrice").value(savedResultItem1.getOriginalPrice()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].salePrice").value(savedResultItem1.getSalePrice()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].recommendId").value(savedRecommendItem1.getId()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].score").value(savedRecommendItem1.getScore()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[0].rank").value(1))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].id").value(savedResultItem2.getId()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].name").value(savedResultItem2.getName()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].imageUrl").value(savedResultItem2.getImageUrl()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].contentUrl").value(savedResultItem2.getContentUrl()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].originalPrice").value(savedResultItem2.getOriginalPrice()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].salePrice").value(savedResultItem2.getSalePrice()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].recommendId").value(savedRecommendItem2.getId()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].score").value(savedRecommendItem2.getScore()))
                .andExpect(jsonPath("recommendItemResponseList.[0].results.[1].rank").value(2))
                .andDo(print());

        // then
        List<RecommendItem> findRecommendItem = recommendItemRepository.findAll();
        Assertions.assertThat(findRecommendItem).hasSize(2);

        List<Long> findResultItemId = findRecommendItem.stream()
                .map(RecommendItem::getResultItem)
                .map(Item::getId)
                .collect(Collectors.toList());
        Assertions.assertThat(findResultItemId).containsOnly(savedResultItem1.getId(), savedResultItem2.getId());
    }

    @DisplayName("추천 상품 삭제 통합테스트")
    @Test
    public void  추천_상품_삭제_통합테스트() throws Exception {

        // given
        Item savedTargetItem = itemRepository.save(new Item(
                "신발",
                "www.image-url1.com",
                "www.content-url1.com",
                30000,
                20000
        ));
        Item savedResultItem1 = itemRepository.save(new Item(
                "양발1",
                "www.image-url2.com",
                "www.content-url2.com",
                20000,
                15000
        ));
        Item savedResultItem2 = itemRepository.save(new Item(
                "양발2",
                "www.image-url3.com",
                "www.content-url3.com",
                10000,
                5000
        ));

        RecommendItem savedRecommendItem1 = recommendItemRepository.save(
                new RecommendItem(savedTargetItem, savedResultItem1, 20)
        );
        RecommendItem savedRecommendItem2 = recommendItemRepository.save(
                new RecommendItem(savedTargetItem, savedResultItem2, 19)
        );


        // when
        mockMvc.perform(delete(
                        "/target-items/{targetId}/recommend-items/{recommendId}",
                        savedTargetItem.getId(),
                        savedRecommendItem2.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print());

        // then
        List<RecommendItem> findRecommendItem = recommendItemRepository.findAll();
        Assertions.assertThat(findRecommendItem).hasSize(1);
        Assertions.assertThat(findRecommendItem.get(0).getId()).isEqualTo(savedRecommendItem1.getId());
    }


}
