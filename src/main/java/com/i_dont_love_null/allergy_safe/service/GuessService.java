package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.GuessFoodResponse;
import com.i_dont_love_null.allergy_safe.model.Profile;
import com.i_dont_love_null.allergy_safe.repository.DiaryRepository;
import com.i_dont_love_null.allergy_safe.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class GuessService {
    private final GuessFoodResponse guessFoodResponse;

    public GuessFoodResponse guessResponse() {
        return guessFoodResponse;
    }

    private final ProfileRepository profileRepository;
    private final DiaryRepository diaryRepository;

    public GuessFoodResponse guessing(Long profileId, LocalDate startDate, LocalDate endDate) {
        Profile profile;
        Optional<Profile> optionalProfile = profileRepository.findById(profileId);

        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        if (startDate.isAfter(endDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "시작 날짜가 끝 날짜보다 앞서야합니다.");
        }
        if (!diaryRepository.existsDiariesByProfileId(profileId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 프로필에 등록된 다이어리가 없습니다.");
        }
        return guessFoodResponse;
    }


}

