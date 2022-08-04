package com.backendcodingtest.codingtest.item.service;

import com.backendcodingtest.codingtest.common.exception.ExceptionMessage;
import com.backendcodingtest.codingtest.item.dto.ItemCreateRequest;
import com.backendcodingtest.codingtest.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.backendcodingtest.codingtest.common.exception.ExceptionMessage.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;


    @Transactional
    public void saveItem(ItemCreateRequest itemCreateRequest) {

        checkDuplicateItemName(itemCreateRequest.getName());
        itemRepository.save(
                itemCreateRequest.toEntity()
        );
//        todo - 추천 아이템들이 레포에 있는지 확인하는 checker 구현
//        List<Long> recommendItemIdList = itemCreateRequest.getRecommendItemRequestList().stream()
//                .map(RecommendItemRequest::getId)
//                .collect(Collectors.toList());
//        if (!itemRepository.findAllById(recommendItemIdList).stream()
//                .map(Item::getId)
//                .anyMatch(itemId -> recommendItemIdList.contains(itemId))) {
//
//        }
//        itemRepository.findAllById(recommendItemIdList).containsAll(recommendItemIdList);
//        List<Long> recommendItemIdList = itemCreateRequest.getRecommendItemRequestList().stream()
//                .map(RecommendItemRequest::getId)
//                .collect(Collectors.toList());
//        List<Item> findRecommendItemList = itemRepository.findAllById(recommendItemIdList);
//
//        for (RecommendItemRequest recommendItemRequest : itemCreateRequest.getRecommendItemRequestList()) {
//            for (Item findRecommendItem : findRecommendItemList) {
//                if (recommendItemRequest.getId() == findRecommendItem.getId()) {
//                    recommendItemRepository.save(RecommendItem.creatRecommendItem(
//                                    savedItem,
//                                    findRecommendItem,
//                                    recommendItemRequest.getScore()
//                            ));
//                }
//            }
//        }

    }

    private void checkDuplicateItemName(String name) {
        if (itemRepository.existsByName(name)) {
            throw DUPLICATE_ITEM_NAME.getException();
        }
    }
}
