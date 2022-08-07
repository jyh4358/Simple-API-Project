package com.backendcodingtest.codingtest.item.service;

import com.backendcodingtest.codingtest.common.util.StringEditor;
import com.backendcodingtest.codingtest.item.dto.ItemCreateAndUpdateRequest;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponse;
import com.backendcodingtest.codingtest.item.dto.ItemDetailResponses;
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


    public ItemDetailResponse findItem(Long id) {

        Item findItem = itemRepository.findById(id).orElseThrow(NOT_FOUNT_ITEM::getException);
        return ItemDetailResponse.of(findItem);
    }


    public ItemDetailResponses findAllItem() {

        List<Item> findAllItem = itemRepository.findAll();
        List<ItemDetailResponse> itemDetailResponseList = findAllItem.stream()
                .map(ItemDetailResponse::of)
                .collect(Collectors.toList());
        return ItemDetailResponses.of(itemDetailResponseList);
    }


    @Transactional
    public void saveItem(ItemCreateAndUpdateRequest itemCreateAndUpdateRequest) {

        checkDuplicateItemName(itemCreateAndUpdateRequest.getName());
        itemRepository.save(
                itemCreateAndUpdateRequest.toEntity()
        );
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
