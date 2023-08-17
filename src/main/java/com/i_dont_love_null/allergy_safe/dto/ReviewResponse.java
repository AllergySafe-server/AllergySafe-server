package com.i_dont_love_null.allergy_safe.dto;

import com.i_dont_love_null.allergy_safe.model.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Service
public class ReviewResponse {
    private Integer star;
    private String content;
    private Integer totalCount;
    private Double average;
    private List<Review> reviews;
}
