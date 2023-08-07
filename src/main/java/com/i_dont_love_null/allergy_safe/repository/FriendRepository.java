package com.i_dont_love_null.allergy_safe.repository;

import com.i_dont_love_null.allergy_safe.model.Friend;
import com.i_dont_love_null.allergy_safe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FriendRepository extends JpaRepository<Friend, Long> {
    Friend findByIdAndUserId(Long id, Long userId);
}
