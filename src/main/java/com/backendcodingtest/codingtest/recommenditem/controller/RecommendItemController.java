package com.backendcodingtest.codingtest.recommenditem.controller;

import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequests;
import com.backendcodingtest.codingtest.recommenditem.dto.response.RecommendItemResponses;
import com.backendcodingtest.codingtest.recommenditem.service.RecommendItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RecommendItemController {

    private final RecommendItemService recommendItemService;

    @GetMapping("/recommend-items/{id}")
    public ResponseEntity<RecommendItemResponses> findRecommendItem(
            @PathVariable String id
    ) {
        RecommendItemResponses recommendItemResponses = recommendItemService.findRecommendItem(id);
        return new ResponseEntity<>(recommendItemResponses, HttpStatus.OK);
    }

    @PostMapping("/recommend-items")
    public ResponseEntity<Void> saveRecommendItem(
            @Valid @RequestBody RecommendItemRequests recommendItemRequests
    ) {
        recommendItemService.saveRecommendItem(recommendItemRequests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
