package com.i_dont_love_null.allergy_safe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SimpleHttp {
    public static String get(String uri) throws IOException {
        HttpURLConnection conn = null;
        BufferedReader br = null;
        InputStreamReader ir = null;
        StringBuilder sb;
        try {
            conn = (HttpURLConnection) new URL(uri).openConnection();

            int resCode = conn.getResponseCode();
            if (resCode != 200) throw new IOException();

            String buffer;
            ir = new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8);
            br = new BufferedReader(ir);
            sb = new StringBuilder();
            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }
            return sb.toString();

        } finally {
            if (Objects.nonNull(conn)) conn.disconnect();
            if (Objects.nonNull(br)) br.close();
            if (Objects.nonNull(ir)) ir.close();
        }

    }
}
