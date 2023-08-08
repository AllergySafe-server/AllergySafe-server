package com.i_dont_love_null.allergy_safe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class SimpleHttp {
    public static String get(String uri) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(uri).openConnection();

        int resCode = conn.getResponseCode();
        if (resCode != 200) throw new IOException();

        String buffer;
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        while ((buffer = br.readLine()) != null) {
            sb.append(buffer);
        }
        return sb.toString();
    }
}
