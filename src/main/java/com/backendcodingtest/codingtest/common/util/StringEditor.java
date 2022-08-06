package com.backendcodingtest.codingtest.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringEditor {
    private static final String COMMA = ",";

    public static List<Long> converterStringToStringList(String id) {

        if (id == null) {
            return new ArrayList<>();
        }

        return Arrays.stream(id.split(COMMA)).map(s -> Long.parseLong(s)).collect(Collectors.toList());
    }
}
