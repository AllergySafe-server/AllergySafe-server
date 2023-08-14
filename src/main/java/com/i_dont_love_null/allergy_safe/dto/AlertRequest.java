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
public class AlertRequest {
    @Schema(description = "경보 기능 대상(Food 또는 Medicine)의 id", example = "1")
    private Long id;

    @Schema(description = "경보 대상 프로필들의 id 목록", example = "[1, 2, 3, 4]")
    private List<Long> profileIdList;
}
