package com.backendcodingtest.codingtest.item.common;

import com.backendcodingtest.codingtest.item.repository.ItemRepository;
import com.backendcodingtest.codingtest.item.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("h2")
@SpringBootTest
public class ServiceBaseTest {

    @Autowired
    protected ItemRepository itemRepository;

}
