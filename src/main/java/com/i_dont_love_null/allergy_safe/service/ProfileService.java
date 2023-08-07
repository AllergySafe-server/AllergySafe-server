package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.dto.ProfileRequest;
import com.i_dont_love_null.allergy_safe.model.Profile;
import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.repository.ProfileRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final IdResponse idResponse;

    public IdResponse createProfile(User user, ProfileRequest profileRequest) {
        idResponse.setId(profileRepository.save(
                Profile.builder()
                        .name(profileRequest.getName())
                        .user(user)
                        .build()
        ).getId());
        return idResponse;
    }

    public IdResponse deleteProfile(User user, Long profileId) {
        checkIfFamily(user, profileId);
        profileRepository.deleteById(profileId);
        idResponse.setId(profileId);

        return idResponse;
    }

    public void checkIfExists(Long profileId) {
        if (profileRepository.findById(profileId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");
    }


    public void checkIfFamily(User user, Long profileId) {
        checkIfExists(profileId);
        if (Objects.isNull(profileRepository.findByIdAndUser(profileId, user)))
            // select * from profile where id=3 and user_id=1
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근할 수 없는 프로필입니다.");

    }
}
