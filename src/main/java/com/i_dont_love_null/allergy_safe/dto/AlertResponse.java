package com.i_dont_love_null.allergy_safe.dto;

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
public class AlertResponse {
    @Schema(description = "선택한 프로필 중 내 가족의 경보 정보를 담은 리스트")
    private List<SimpleProfile> family;

    @Schema(description = "선택한 프로필 중 내 친구의 경보 정보를 담은 리스트")
    private List<SimpleProfile> friend;
}
