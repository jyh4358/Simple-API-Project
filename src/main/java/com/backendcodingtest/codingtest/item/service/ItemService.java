package com.backendcodingtest.codingtest.item.service;

import com.backendcodingtest.codingtest.common.util.StringEditor;
import com.backendcodingtest.codingtest.item.dto.ItemCreateAndUpdateRequest;
import com.backendcodingtest.codingtest.item.dto.ItemDetail;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponse;
import com.backendcodingtest.codingtest.item.model.Item;
import com.backendcodingtest.codingtest.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.backendcodingtest.codingtest.common.exception.ExceptionMessage.DUPLICATE_ITEM_NAME;
import static com.backendcodingtest.codingtest.common.exception.ExceptionMessage.NOT_FOUNT_ITEM;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;


    public ItemDetailResponse findItem(String id) {

        List<Long> ids = StringEditor.converterStringToStringList(id);
        List<Item> findItemList = itemRepository.findAllById(ids);
        return ItemDetailResponse.of(findItemList.stream().map(ItemDetail::of).collect(Collectors.toList()));
    }


    @Transactional
    public void saveItem(ItemCreateAndUpdateRequest itemCreateAndUpdateRequest) {

        checkDuplicateItemName(itemCreateAndUpdateRequest.getName());
        itemRepository.save(
                itemCreateAndUpdateRequest.toEntity()
        );
//        todo - 추천 아이템들이 레포에 있는지 확인하는 checker 구현
//        List<Long> recommendItemIdList = itemCreateAndUpdateRequest.getRecommendItemRequestList().stream()
//                .map(RecommendItemRequest::getId)
//                .collect(Collectors.toList());
//        if (!itemRepository.findAllById(recommendItemIdList).stream()
//                .map(Item::getId)
//                .anyMatch(itemId -> recommendItemIdList.contains(itemId))) {
//
//        }
//        itemRepository.findAllById(recommendItemIdList).containsAll(recommendItemIdList);
//        List<Long> recommendItemIdList = itemCreateAndUpdateRequest.getRecommendItemRequestList().stream()
//                .map(RecommendItemRequest::getId)
//                .collect(Collectors.toList());
//        List<Item> findRecommendItemList = itemRepository.findAllById(recommendItemIdList);
//
//        for (RecommendItemRequest recommendItemRequest : itemCreateAndUpdateRequest.getRecommendItemRequestList()) {
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

    @Transactional
    public void updateItem(Long id, ItemCreateAndUpdateRequest itemCreateAndUpdateRequest) {

        Item findItem = itemRepository.findById(id).orElseThrow(NOT_FOUNT_ITEM::getException);
        checkDuplicateItemName(itemCreateAndUpdateRequest.getName());

        findItem.updateItem(
                itemCreateAndUpdateRequest.getName(),
                itemCreateAndUpdateRequest.getImageUrl(),
                itemCreateAndUpdateRequest.getContentUrl(),
                itemCreateAndUpdateRequest.getOriginalPrice(),
                itemCreateAndUpdateRequest.getSalePrice()
        );
    }

    @Transactional
    public void deleteItem(Long id) {
        Item findItem = itemRepository.findById(id).orElseThrow(NOT_FOUNT_ITEM::getException);
        itemRepository.delete(findItem);
    }


    private void checkDuplicateItemName(String name) {
        if (itemRepository.existsByName(name)) {
            throw DUPLICATE_ITEM_NAME.getException();
        }
    }
}
