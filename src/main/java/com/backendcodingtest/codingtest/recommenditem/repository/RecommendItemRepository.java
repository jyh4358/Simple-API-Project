package com.backendcodingtest.codingtest.recommenditem.repository;

import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendItemRepository extends JpaRepository<RecommendItem, Long> {
    void deleteAllByTargetItemId(Long id);

}
