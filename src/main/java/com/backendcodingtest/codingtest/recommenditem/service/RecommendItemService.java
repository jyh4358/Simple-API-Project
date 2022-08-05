package com.backendcodingtest.codingtest.recommenditem.service;

import com.backendcodingtest.codingtest.item.dto.ItemDetail;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.item.repository.ItemRepository;
import com.backendcodingtest.codingtest.recommenditem.dto.*;
import com.backendcodingtest.codingtest.recommenditem.dto.response.RecommendItemResponse;
import com.backendcodingtest.codingtest.recommenditem.dto.response.RecommendItemResponses;
import com.backendcodingtest.codingtest.recommenditem.dto.response.ResultItemResponse;
import com.backendcodingtest.codingtest.recommenditem.model.RecommendItem;
import com.backendcodingtest.codingtest.recommenditem.repository.RecommendItemRepository;
import com.backendcodingtest.codingtest.recommenditem.repository.RecommendItemSearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.backendcodingtest.codingtest.common.exception.ExceptionMessage.NOT_FOUNT_ITEM;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendItemService {

    private final RecommendItemRepository recommendItemRepository;
    private final RecommendItemSearchRepository recommendItemSearchRepository;
    private final ItemRepository itemRepository;

    public RecommendItemResponses findRecommendItem(String id) {
        List<Long> itemIdList = converterStringToStringList(id);

        // todo - db에 해당 item이 있는지 check 구현 -> 생각해보니 필요없을 듯

        List<Item> findTargetItemList = itemRepository.findAllById(itemIdList);

        List<RecommendItemResponse> recommendItemResponseList = createRecommendItemResponses(findTargetItemList);
        return RecommendItemResponses.from(recommendItemResponseList);
    }


    @Transactional
    public void saveRecommendItem(RecommendItemRequests recommendItemRequests) {
        Item targetItem = itemRepository.findById(recommendItemRequests.getId()).orElseThrow(NOT_FOUNT_ITEM::getException);

        List<RecommendItemRequest> recommendItemRequestList = recommendItemRequests.getRecommendItemRequestList();
        List<Long> recommendItemId = recommendItemRequestList.stream()
                .map(recommendItemRequest -> recommendItemRequest.getId())
                .collect(Collectors.toList());
        List<Item> resultItemList = itemRepository.findAllById(recommendItemId);

        recommendItemRepository.deleteAllByTargetItemId(targetItem.getId());

        recommendItemCreateAndSave(targetItem, recommendItemRequestList, resultItemList);
    }



    /*
        ========== 비즈니스 로직 ============
     */
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

    public List<Long> converterStringToStringList(String id) {
        if (id == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(id.split(",")).map(s -> Long.parseLong(s)).collect(Collectors.toList());
    }

    private List<RecommendItemResponse> createRecommendItemResponses(List<Item> findTargetItemList) {
        List<RecommendItemResponse> recommendItemResponseList = new ArrayList<>();
        for (Item targetItem : findTargetItemList) {
            List<RecommendItem> recommendItemWithResult = recommendItemSearchRepository.findRecommendItemWithResult(targetItem.getId());
            List<ResultItemResponse> resultItemResponse = createResultItemResponse(recommendItemWithResult);
            recommendItemResponseList.add(RecommendItemResponse.of(ItemDetail.of(targetItem), resultItemResponse));
        }

        return recommendItemResponseList;
    }

    private List<ResultItemResponse> createResultItemResponse(List<RecommendItem> recommendItemWithResult) {
        List<ResultItemResponse> resultItemResponse = new ArrayList<>();
        for (int i = 0; i < recommendItemWithResult.size(); i++) {
            resultItemResponse.add(ResultItemResponse.of(
                            recommendItemWithResult.get(i).getResultItem(),
                            recommendItemWithResult.get(i),
                            i + 1
                    )
            );
        }

        return resultItemResponse;
    }
}
