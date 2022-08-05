package com.backendcodingtest.codingtest.recommenditem.repository;

import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.backendcodingtest.codingtest.item.model.QItem.item;
import static com.backendcodingtest.codingtest.recommenditem.model.QRecommendItem.recommendItem;

@Repository
@RequiredArgsConstructor
public class RecommendItemSearchRepository {
    private final JPAQueryFactory queryFactory;

    public List<RecommendItem> findRecommendItemWithResult(Long targetItemId) {
        return queryFactory.selectFrom(recommendItem)
                .leftJoin(recommendItem.resultItem, item)
                .where(recommendItem.targetItem.id.eq(targetItemId))
                .orderBy(recommendItem.score.desc())
                .fetch();
    }



}
