package com.backendcodingtest.codingtest.common.basetest;

import com.backendcodingtest.codingtest.common.database.DatabaseCleaner;
import com.backendcodingtest.codingtest.common.testconfig.RestDocsConfiguration;
import com.backendcodingtest.codingtest.item.repository.ItemRepository;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import com.backendcodingtest.codingtest.recommenditem.repository.RecommendItemRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("h2")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
public class DocumentBaseTest {

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected DatabaseCleaner databaseCleaner;

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected RecommendItemRepository recommendItemRepository;
}
