package com.backendcodingtest.codingtest.recommenditem.repository;

import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecommendItemRepository extends JpaRepository<RecommendItem, Long> {
    void deleteAllByTargetItemId(Long id);

    boolean existsByTargetItemId(Long id);

    Optional<RecommendItem> findByTargetItemIdAndResultItemId(Long targetId, Long resultId);

}
