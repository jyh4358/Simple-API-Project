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

    @GetMapping("/target-items/{id}/recommend-items")
    public ResponseEntity<RecommendItemResponses> findRecommendItem(
            @PathVariable String id
    ) {
        RecommendItemResponses recommendItemResponses = recommendItemService.findRecommendItem(id);
        return new ResponseEntity<>(recommendItemResponses, HttpStatus.OK);
    }

    @PostMapping("/target-items/recommend-items")
    public ResponseEntity<Void> saveRecommendItem(
            @Valid @RequestBody RecommendItemRequests recommendItemRequests
    ) {
        recommendItemService.saveRecommendItem(recommendItemRequests);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/target-items/{targetId}/recommend-items/{recommendId}")
    public ResponseEntity<Void> deleteRecommendItem(
            @PathVariable Long targetId,
            @PathVariable Long recommendId
    ) {
        recommendItemService.deleteRecommendItem(targetId, recommendId);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
