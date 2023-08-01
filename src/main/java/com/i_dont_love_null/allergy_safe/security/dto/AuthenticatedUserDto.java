package com.i_dont_love_null.allergy_safe.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

    private String email;
    private String password;
}
