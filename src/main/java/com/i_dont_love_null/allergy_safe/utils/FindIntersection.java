package com.i_dont_love_null.allergy_safe.utils;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FindIntersection {
    public static List<String> find(List<String> list1, List<String> list2) {
        return list1.stream()
                .distinct()
                .filter(list2::contains)
                .collect(Collectors.toList());
    }
}
