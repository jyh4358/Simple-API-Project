package com.backendcodingtest.codingtest.document.recommenditem;

import com.backendcodingtest.codingtest.common.basetest.DocumentBaseTest;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequest;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequests;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecommendItemDocumentTest extends DocumentBaseTest {

    @AfterEach
    void tearDown() {
        databaseCleaner.clean();
    }

    @DisplayName("추천 상품 등록 문서화 테스트")
    @Test
    public void  추천_상품_등록_문서화() throws Exception {

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

        // when&&then
        mockMvc.perform(post("/target-items/{id}/recommend-items", savedTargetItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(recommendItemRequests)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("recommendItem-save",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("recommendItemRequestList.[0].id").type("Long").description("상품명"),
                                fieldWithPath("recommendItemRequestList.[0].score").type("int").description("해당 연관 상품코드의 연관도 점수"),
                                fieldWithPath("recommendItemRequestList.[1].id").type("Long").description("상품명"),
                                fieldWithPath("recommendItemRequestList.[1].score").type("int").description("해당 연관 상품코드의 연관도 점수")
                        )
                ));
    }

    @DisplayName("상품과 추천 상품 조회 문서화 테스트")
    @Test
    public void  상품과_추천_상품_조회_문서화() throws Exception {

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
                new com.backendcodingtest.codingtest.recommenditem.model.RecommendItem(savedTargetItem, savedResultItem1, 20)
        );
        RecommendItem savedRecommendItem2 = recommendItemRepository.save(
                new com.backendcodingtest.codingtest.recommenditem.model.RecommendItem(savedTargetItem, savedResultItem2, 19)
        );

        // when&&then
        mockMvc.perform(get("/target-items/recommend-items?id=" + savedTargetItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("recommendItem-list",
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        requestParameters(
                                parameterWithName("id").description("선택한 상품들의 식별자 (콤마','로 연결)")
                        ),
                        responseFields(
                                fieldWithPath("recommendItemResponseList.[0].target.id").type("Long").description("상품 식별자"),
                                fieldWithPath("recommendItemResponseList.[0].target.name").type("String").description("상품 이미지 url"),
                                fieldWithPath("recommendItemResponseList.[0].target.imageUrl").type("String").description("상품 이미지 url"),
                                fieldWithPath("recommendItemResponseList.[0].target.contentUrl").type("String").description("상품 설명 페이지 url"),
                                fieldWithPath("recommendItemResponseList.[0].target.originalPrice").type("int").description("상품 가격"),
                                fieldWithPath("recommendItemResponseList.[0].target.salePrice").type("int").description("상품 판매 가격"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].id").type("Long").description("상품 식별자"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].name").type("String").description("상품 이미지 url"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].imageUrl").type("String").description("상품 이미지 url"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].contentUrl").type("String").description("상품 설명 페이지 url"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].originalPrice").type("int").description("상품 가격"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].salePrice").type("int").description("상품 판매 가격"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].recommendId").type("Long").description("상품 식별자"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].score").type("int").description("해당 연관 상품코드의 연관도 점수"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].rank").type("int").description("해당 연관 상품코드의 연관도 순위"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].id").type("Long").description("상품 식별자"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].name").type("String").description("상품 이미지 url"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].imageUrl").type("String").description("상품 이미지 url"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].contentUrl").type("String").description("상품 설명 페이지 url"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].originalPrice").type("int").description("상품 가격"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].salePrice").type("int").description("상품 판매 가격"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].recommendId").type("Long").description("상품 식별자"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].score").type("int").description("해당 연관 상품코드의 연관도 점수"),
                                fieldWithPath("recommendItemResponseList.[0].results.[0].rank").type("int").description("해당 연관 상품코드의 연관도 순위")
                        )
                ));
    }

    @DisplayName("추천 상품 수정 통합테스트")
    @Test
    public void  추천_상품_수정_통합테스트() throws Exception {

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

        // when&&then
        mockMvc.perform(put("/target-items/{id}/recommend-items", targetItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(recommendItemRequests)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("recommendItem-modify",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("recommendItemRequestList.[0].id").type("상품 식별자").description("상품명"),
                                fieldWithPath("recommendItemRequestList.[0].score").type("int").description("해당 연관 상품코드의 연관도 점수"),
                                fieldWithPath("recommendItemRequestList.[1].id").type("상품 식별자").description("상품명"),
                                fieldWithPath("recommendItemRequestList.[1].score").type("int").description("해당 연관 상품코드의 연관도 점수")
                        )
                ));
    }


    @DisplayName("추천 상품 삭제 문서화 테스트")
    @Test
    public void  추천_상품_삭제_문서화() throws Exception {

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


        // when&&then
        mockMvc.perform(delete("/target-items/{id}/recommend-items/{recommendId}", savedTargetItem.getId(), savedRecommendItem1.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("recommendItem-delete"));
    }
}
