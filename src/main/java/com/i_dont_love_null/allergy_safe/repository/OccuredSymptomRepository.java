package com.i_dont_love_null.allergy_safe.repository;

import com.i_dont_love_null.allergy_safe.model.OccuredSymptom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OccuredSymptomRepository extends JpaRepository<OccuredSymptom, Long> {

    void deleteBySymptomId(Long symptomId);

    Boolean existsByDiaryId(Long id);
}
