package com.backendcodingtest.codingtest.document.item;

import com.backendcodingtest.codingtest.common.basetest.DocumentBaseTest;
import com.backendcodingtest.codingtest.item.dto.ItemCreateAndUpdateRequest;
import com.backendcodingtest.codingtest.item.model.Item;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ItemDocumentTest extends DocumentBaseTest {


    @AfterEach
    void tearDown() {
        databaseCleaner.clean();
    }

    @DisplayName("상품 등록 문서화 테스트")
    @Test
    public void 상품_등록_문서화() throws Exception {

        // given
        ItemCreateAndUpdateRequest itemCreateAndUpdateRequest = new ItemCreateAndUpdateRequest(
                "신발",
                "www.image-url.com",
                "www.content-url.com",
                20000,
                10000
        );

        // when&&then
        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(itemCreateAndUpdateRequest)))
                .andExpect(status().isCreated())
                .andDo(print())
                .andDo(document("item-save",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("name").type("String").description("상품명"),
                                fieldWithPath("imageUrl").type("String").description("상품 이미지 url"),
                                fieldWithPath("contentUrl").type("String").description("상품 설명 페이지 url"),
                                fieldWithPath("originalPrice").type("int").description("상품 가격"),
                                fieldWithPath("salePrice").type("int").description("상품 판매 가격")
                        )
                ));
    }

    @DisplayName("상품 조회 문서화 테스트")
    @Test
    public void 상품_조회_문서화() throws Exception {

        // given
        Item savedItem = itemRepository.save(new Item(
                "양발",
                "www.image-url.com",
                "www.content-url.com",
                10000,
                5000
        ));

        // when&&then
        mockMvc.perform(get("/items/{id}", savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("item-detail",
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("id").type("Long").description("상품 식별자"),
                                fieldWithPath("name").type("String").description("상품명"),
                                fieldWithPath("imageUrl").type("String").description("상품 이미지 url"),
                                fieldWithPath("contentUrl").type("String").description("상품 설명 페이지 url"),
                                fieldWithPath("originalPrice").type("int").description("상품 가격"),
                                fieldWithPath("salePrice").type("int").description("상품 판매 가격")
                        )
                ));
    }

    @DisplayName("상품 전체 조회 문서화 테스트")
    @Test
    public void 상품_전체_조회_문서화() throws Exception {

        // given
        Item savedItem1 = itemRepository.save(new Item(
                "양발",
                "www.image-url.com",
                "www.content-url.com",
                10000,
                5000
        ));
        Item savedItem2 = itemRepository.save(new Item(
                "양발2",
                "www.image-url.com",
                "www.content-url.com",
                10000,
                5000
        ));

        // when&&then
        mockMvc.perform(get("/items")
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("item-list",
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("응답 데이터의 타입필드, 응답 객체는 JSON 형태로 응답")
                        ),
                        responseFields(
                                fieldWithPath("itemDetailResponseList.[0].id").type("Long").description("상품 식별자"),
                                fieldWithPath("itemDetailResponseList.[0].name").type("String").description("상품명"),
                                fieldWithPath("itemDetailResponseList.[0].imageUrl").type("String").description("상품 이미지 url"),
                                fieldWithPath("itemDetailResponseList.[0].contentUrl").type("String").description("상품 설명 페이지 url"),
                                fieldWithPath("itemDetailResponseList.[0].originalPrice").type("int").description("상품 가격"),
                                fieldWithPath("itemDetailResponseList.[0].salePrice").type("int").description("상품 판매 가격"),
                                fieldWithPath("itemDetailResponseList.[1].id").type("Long").description("상품 식별자"),
                                fieldWithPath("itemDetailResponseList.[1].name").type("String").description("상품명"),
                                fieldWithPath("itemDetailResponseList.[1].imageUrl").type("String").description("상품 이미지 url"),
                                fieldWithPath("itemDetailResponseList.[1].contentUrl").type("String").description("상품 설명 페이지 url"),
                                fieldWithPath("itemDetailResponseList.[1].originalPrice").type("int").description("상품 가격"),
                                fieldWithPath("itemDetailResponseList.[1].salePrice").type("int").description("상품 판매 가격")
                        )
                ));
    }

    @DisplayName("상품 수정 문서화 테스트")
    @Test
    public void 상품_수정_문서화() throws Exception {

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

        // when&&then
        mockMvc.perform(put("/items/{id}", savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(itemCreateAndUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("item-modify",
                        requestHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("요청 데이터의 타입필드, 요청 객체는 JSON 형태로 요청")
                        ),
                        requestFields(
                                fieldWithPath("name").type("String").description("상품명"),
                                fieldWithPath("imageUrl").type("String").description("상품 이미지 url"),
                                fieldWithPath("contentUrl").type("String").description("상품 설명 페이지 url"),
                                fieldWithPath("originalPrice").type("int").description("상품 가격"),
                                fieldWithPath("salePrice").type("int").description("상품 판매 가격")
                        )
                ));
    }

    @DisplayName("상품 삭제 문서화 테스트")
    @Test
    public void 상품_삭제_문서화() throws Exception {

        // given
        Item savedItem = itemRepository.save(new Item(
                "양발",
                "www.image-url.com",
                "www.content-url.com",
                10000,
                5000
        ));

        // when&&then
        mockMvc.perform(delete("/items/{id}", savedItem.getId())
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("item-delete"));
    }

}
