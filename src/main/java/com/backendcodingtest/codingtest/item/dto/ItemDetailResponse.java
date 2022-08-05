package com.backendcodingtest.codingtest.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemDetailResponse {
    private List<ItemDetail> itemDetailList = new ArrayList<>();

    public static ItemDetailResponse of(List<ItemDetail> itemDetailList) {
        return new ItemDetailResponse(itemDetailList);
    }
}
