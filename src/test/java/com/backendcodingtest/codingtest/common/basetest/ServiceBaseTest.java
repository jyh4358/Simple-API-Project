package com.backendcodingtest.codingtest.common.basetest;

import com.backendcodingtest.codingtest.common.database.DatabaseCleaner;
import com.backendcodingtest.codingtest.item.repository.ItemRepository;
import com.backendcodingtest.codingtest.recommenditem.repository.RecommendItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest
public class ServiceBaseTest {

    @Autowired
    protected ItemRepository itemRepository;

    @Autowired
    protected RecommendItemRepository recommendItemRepository;

    @Autowired
    protected DatabaseCleaner databaseCleaner;
}
