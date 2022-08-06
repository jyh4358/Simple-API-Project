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
public class ItemDetailResponses {
    private List<ItemDetailResponse> itemDetailResponseList = new ArrayList<>();

    public static ItemDetailResponses of(List<ItemDetailResponse> itemDetailResponseList) {
        return new ItemDetailResponses(itemDetailResponseList);
    }
}
