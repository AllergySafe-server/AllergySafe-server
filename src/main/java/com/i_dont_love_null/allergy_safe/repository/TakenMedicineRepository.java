package com.i_dont_love_null.allergy_safe.repository;

import com.i_dont_love_null.allergy_safe.model.TakenMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TakenMedicineRepository extends JpaRepository<TakenMedicine, Long> {
}
