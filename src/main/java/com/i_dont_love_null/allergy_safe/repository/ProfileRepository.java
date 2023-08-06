package com.i_dont_love_null.allergy_safe.repository;

import com.i_dont_love_null.allergy_safe.model.Profile;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProfileRepository extends JpaRepository<Profile, Long> {

}
