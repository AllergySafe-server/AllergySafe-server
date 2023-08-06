package com.i_dont_love_null.allergy_safe.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Service
public class FoodFromApiResponse {
    private String name;
    private List<String> materials;
    private List<String> allergies;
}
