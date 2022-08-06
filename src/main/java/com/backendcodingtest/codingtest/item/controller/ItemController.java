package com.backendcodingtest.codingtest.item.controller;

import com.backendcodingtest.codingtest.item.dto.ItemCreateAndUpdateRequest;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponse;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponses;
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
    public ResponseEntity<ItemDetailResponse> findItem(
            @PathVariable Long id
    ) {

        ItemDetailResponse itemDetailResponse = itemService.findItem(id);
        return new ResponseEntity<>(itemDetailResponse, HttpStatus.OK);
    }

    @PostMapping("/items")
    public ResponseEntity<Void> saveItem(
            @Valid @RequestBody ItemCreateAndUpdateRequest itemCreateAndUpdateRequest
    ) {

        itemService.saveItem(itemCreateAndUpdateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<Void> updateItem(
            @PathVariable Long id,
            @Valid @RequestBody ItemCreateAndUpdateRequest itemCreateAndUpdateRequest
    ) {

        itemService.updateItem(id, itemCreateAndUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id
    ) {

        itemService.deleteItem(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
