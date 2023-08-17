package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.dto.ReviewRequest;
import com.i_dont_love_null.allergy_safe.dto.ReviewResponse;
import com.i_dont_love_null.allergy_safe.model.Review;
import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.properties.AppProperties;
import com.i_dont_love_null.allergy_safe.repository.ReviewRepository;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    private final AppProperties appProperties;

    private final IdResponse idResponse;

    private final ReviewResponse reviewResponse;

    public IdResponse createReview(User user, ReviewRequest reviewRequest) {
        if (Objects.nonNull(user.getReview())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 리뷰를 작성했습니다.");
        LocalDateTime currentDateTime = LocalDateTime.now();
        boolean isLocal = appProperties.getAppDomain().contains("localhost");
        if (!isLocal) {
            currentDateTime = currentDateTime.plusHours(9);
        }

        Review review = Review.builder()
                .star(reviewRequest.getStar())
                .content(reviewRequest.getContent())
                .datetime(currentDateTime)
                .build();

        Review createdReview = reviewRepository.save(review);
        User newUser = userRepository.save(user.toBuilder()
                .review(createdReview)
                .build());


        idResponse.setId(newUser.getReview().getId());
        return idResponse;
    }

    public ReviewResponse getReview(User user) {
        Review myReview = user.getReview();
        if (Objects.isNull(myReview)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "아직 리뷰를 작성하지 않았습니다.");
        int totalCount = reviewRepository.countAllBy();
        int allStartsSum = reviewRepository.sumAllStars();
        double average = (double) allStartsSum / totalCount;
        average = Math.round(average * 100) / 100.0;

        reviewResponse.setStar(myReview.getStar());
        reviewResponse.setContent(myReview.getContent());
        reviewResponse.setTotalCount(totalCount);
        reviewResponse.setAverage(average);
        reviewResponse.setReviews(reviewRepository.findAllByIdNotOrderByDatetime(myReview.getId()));
        return reviewResponse;
    }
}
