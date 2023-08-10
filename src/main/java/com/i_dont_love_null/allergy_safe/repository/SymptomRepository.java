package com.i_dont_love_null.allergy_safe.repository;

import com.i_dont_love_null.allergy_safe.model.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
}
