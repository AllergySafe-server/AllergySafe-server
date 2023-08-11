package com.i_dont_love_null.allergy_safe.repository;

import com.i_dont_love_null.allergy_safe.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByProfileIdAndDate(Long profileId, LocalDate date);

    Boolean existsByProfileIdAndDate(Long profileId, LocalDate date);

    List<Diary> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);


}
