package com.i_dont_love_null.allergy_safe.security.utils;

import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class SecurityConstants {
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";

    private static final String[] GET_URI_WHITE_LIST = new String[]{"/api/health", "/api/user/validate", "/api/user/password-reset", "/api/user/profile/share/**", "/api/swagger/**"};
    private static final String[] POST_URI_WHITE_LIST = new String[]{"/api/user", "/api/auth/login", "/api/user/password-reset"};

    public static String[] getGetUriWhiteList() {
        return GET_URI_WHITE_LIST.clone();
    }

    public static String[] getPostUriWhiteList() {
        return POST_URI_WHITE_LIST.clone();
    }

    public static String getAuthenticatedPassword() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getPassword();
    }

}
