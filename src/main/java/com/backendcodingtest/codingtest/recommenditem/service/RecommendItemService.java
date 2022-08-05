package com.backendcodingtest.codingtest.recommenditem.service;

import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.item.repository.ItemRepository;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequest;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequests;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import com.backendcodingtest.codingtest.recommenditem.repository.RecommendItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.backendcodingtest.codingtest.common.exception.ExceptionMessage.NOT_FOUNT_ITEM;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendItemService {

    private final RecommendItemRepository recommendItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public void saveRecommendItem(RecommendItemRequests recommendItemRequests) {
        Item targetItem = itemRepository.findById(recommendItemRequests.getId()).orElseThrow(NOT_FOUNT_ITEM::getException);

        List<RecommendItemRequest> recommendItemRequestList = recommendItemRequests.getRecommendItemRequestList();
        List<Long> recommendItemId = recommendItemRequestList.stream()
                .map(recommendItemRequest -> recommendItemRequest.getId())
                .collect(Collectors.toList());
        List<Item> resultItemList = itemRepository.findAllById(recommendItemId);

        //todo - 추천 상품이 없을 경우 예외 처리 구현

        recommendItemRepository.deleteAllByTargetItemId(targetItem.getId());

        recommendItemCreateAndSave(targetItem, recommendItemRequestList, resultItemList);
    }




    private void recommendItemCreateAndSave(Item targetItem, List<RecommendItemRequest> recommendItemRequestList, List<Item> resultItemList) {
        List<RecommendItem> recommendItemList = new ArrayList<>();
        for (Item resultItem : resultItemList) {
            for (RecommendItemRequest recommendItemRequest : recommendItemRequestList) {
                if (resultItem.getId() == recommendItemRequest.getId()) {
                    recommendItemList.add(RecommendItem.creatRecommendItem(
                            targetItem,
                            resultItem,
                            recommendItemRequest.getScore()
                    ));
                }
            }
        }
        recommendItemRepository.saveAll(recommendItemList);
    }
}
