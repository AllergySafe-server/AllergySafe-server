package com.i_dont_love_null.allergy_safe.utils;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EntityListToStringList {
    public List<String> convertWithName(List<? extends Nameable> entityList) {
        List<String> stringList = new ArrayList<>();

        for (Nameable entity : entityList) {
            stringList.add(entity.getName());
        }

        return stringList;
    }
}
