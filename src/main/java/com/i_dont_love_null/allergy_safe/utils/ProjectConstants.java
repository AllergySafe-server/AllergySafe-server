package com.i_dont_love_null.allergy_safe.utils;

import java.util.Locale;


public final class ProjectConstants {


    public static final String DEFAULT_ENCODING = "UTF-8";

    public static final Locale KR_LOCALE = new Locale.Builder().setLanguage("ko").setRegion("KR").build();

    private ProjectConstants() {

        throw new UnsupportedOperationException();
    }

}
