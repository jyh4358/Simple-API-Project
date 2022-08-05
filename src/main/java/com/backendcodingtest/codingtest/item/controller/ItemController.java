package com.backendcodingtest.codingtest.item.controller;

import com.backendcodingtest.codingtest.item.dto.ItemCreateRequest;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponse;
import com.backendcodingtest.codingtest.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/{id}")
    public ResponseEntity<ItemDetailResponse> findItem(@PathVariable String id) {

        ItemDetailResponse itemDetailResponse = itemService.findItem(id);

        return new ResponseEntity<>(itemDetailResponse, HttpStatus.OK);
    }


    @PostMapping("/items")
    public ResponseEntity<Void> saveItem(@Valid @RequestBody ItemCreateRequest itemCreateRequest) {

        itemService.saveItem(itemCreateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
