package com.backendcodingtest.codingtest.item.controller;

import com.backendcodingtest.codingtest.item.dto.ItemCreateRequest;
import com.backendcodingtest.codingtest.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;


    @PostMapping("/items")
    public ResponseEntity<Void> saveItem(@Valid @RequestBody ItemCreateRequest itemCreateRequest) {

        itemService.saveItem(itemCreateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
