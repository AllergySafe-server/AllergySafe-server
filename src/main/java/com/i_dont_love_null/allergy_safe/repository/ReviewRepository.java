package com.i_dont_love_null.allergy_safe.repository;

import com.i_dont_love_null.allergy_safe.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByIdNotOrderByDatetime(Long id);

    @Query("SELECT SUM(star) FROM Review")
    Integer sumAllStars();

    Integer countAllBy();
}
