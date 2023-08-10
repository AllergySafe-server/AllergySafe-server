package com.i_dont_love_null.allergy_safe.dto;

import com.i_dont_love_null.allergy_safe.model.Profile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Service
public class ProfileListResponse {
    @Schema(description = "내 가족의 프로필 목록")
    private List<Profile> family;

    @Schema(description = "내 친구의 프로필 목록")
    private List<Profile> friend;
}
