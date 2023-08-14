package com.i_dont_love_null.allergy_safe.repository;

import com.i_dont_love_null.allergy_safe.model.IngestedFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IngestedFoodRepository extends JpaRepository<IngestedFood, Long> {

    void deleteByFoodId(Long foodId);

    List<IngestedFood> findAllByDiaryId(Long diaryId);
}
