package com.i_dont_love_null.allergy_safe.security.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public class SecurityConstants {
    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String HEADER_STRING = "Authorization";


    private SecurityConstants() {

        throw new UnsupportedOperationException();
    }

    public static String getAuthenticatedPassword() {

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return userDetails.getPassword();
    }

}
