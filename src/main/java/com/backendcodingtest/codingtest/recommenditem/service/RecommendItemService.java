package com.backendcodingtest.codingtest.recommenditem.service;

import com.backendcodingtest.codingtest.common.util.StringEditor;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponse;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.item.repository.ItemRepository;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequest;
import com.backendcodingtest.codingtest.recommenditem.dto.RecommendItemRequests;
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
import java.util.List;
import java.util.stream.Collectors;

import static com.backendcodingtest.codingtest.common.exception.ExceptionMessage.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RecommendItemService {

    private final RecommendItemRepository recommendItemRepository;
    private final RecommendItemSearchRepository recommendItemSearchRepository;
    private final ItemRepository itemRepository;

    public RecommendItemResponses findRecommendItems(String id) {
        List<Long> itemIdList = StringEditor.converterStringToStringList(id);
        // todo - db에 해당 item이 있는지 check 구현 -> 생각해보니 필요없을 듯
        List<Item> findTargetItemList = itemRepository.findAllById(itemIdList);

        List<RecommendItemResponse> recommendItemResponseList = createRecommendItemResponses(findTargetItemList);
        return RecommendItemResponses.from(recommendItemResponseList);
    }



    @Transactional
    public void saveRecommendItem(Long targetId, RecommendItemRequests recommendItemRequests) {

        Item targetItem = itemRepository.findById(targetId).orElseThrow(NOT_FOUNT_ITEM::getException);
        checkExistRecommendItem(targetItem.getId());
        checkBadRequestRecommendItem(targetItem.getId(), recommendItemRequests);
        checkDuplicateItem(recommendItemRequests);

        recommendItemCreateAndSave(targetItem, recommendItemRequests);
    }




    @Transactional
    public void updateRecommendItem(Long targetId, RecommendItemRequests recommendItemRequests) {

        Item targetItem = itemRepository.findById(targetId).orElseThrow(NOT_FOUNT_ITEM::getException);
        checkBadRequestRecommendItem(targetItem.getId(), recommendItemRequests);

        recommendItemRepository.deleteAllByTargetItemId(targetItem.getId());

        recommendItemCreateAndSave(targetItem, recommendItemRequests);
    }

    @Transactional
    public void deleteRecommendItem(Long targetId, Long recommendId) {
        itemRepository.findById(targetId).orElseThrow(NOT_FOUNT_ITEM::getException);
        RecommendItem findRecommendItem = recommendItemRepository.findById(recommendId).orElseThrow(NOT_FOUNT_RECOMMEND_ITEM::getException);

        recommendItemRepository.delete(findRecommendItem);
    }



    /*
        ========== 비즈니스 로직 ============
     */
    private void recommendItemCreateAndSave(Item targetItem, RecommendItemRequests recommendItemRequests) {

        List<RecommendItemRequest> recommendItemRequestList = recommendItemRequests.getRecommendItemRequestList();
        List<Long> recommendItemId = recommendItemRequestList.stream()
                .map(recommendItemRequest -> recommendItemRequest.getId())
                .collect(Collectors.toList());
        List<Item> resultItemList = itemRepository.findAllById(recommendItemId);


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


    private List<RecommendItemResponse> createRecommendItemResponses(List<Item> findTargetItemList) {
        List<RecommendItemResponse> recommendItemResponseList = new ArrayList<>();
        for (Item targetItem : findTargetItemList) {
            List<RecommendItem> recommendItemWithResult = recommendItemSearchRepository.findRecommendItemWithResult(targetItem.getId());
            List<ResultItemResponse> resultItemResponse = createResultItemResponse(recommendItemWithResult);
            recommendItemResponseList.add(RecommendItemResponse.of(ItemDetailResponse.of(targetItem), resultItemResponse));
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

    private void checkExistRecommendItem(Long targetId) {
        if (recommendItemRepository.existsByTargetItemId(targetId)) {
            throw EXIST_RECOMMEND_ITEM.getException();
        }
    }

    private void checkBadRequestRecommendItem(Long id, RecommendItemRequests recommendItemRequests) {
        if (recommendItemRequests.getRecommendItemRequestList().stream()
                .map(RecommendItemRequest::getId)
                .anyMatch(recommendItemId -> recommendItemId == id)) {
            throw BAD_REQUEST_RECOMMEND_ITEM.getException();
        }
    }

    private void checkDuplicateItem(RecommendItemRequests recommendItemRequests) {
        List<Long> recommendItemIdList = recommendItemRequests.getRecommendItemRequestList().stream()
                .map(RecommendItemRequest::getId)
                .collect(Collectors.toList());
        if (recommendItemIdList.size() != recommendItemIdList.stream().distinct().count()) {
            throw BAD_REQUEST_DUPLICATE_RECOMMEND_ITEM.getException();
        }
    }

}
