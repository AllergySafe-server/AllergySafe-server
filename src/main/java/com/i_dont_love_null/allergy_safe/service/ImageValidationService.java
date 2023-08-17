package com.i_dont_love_null.allergy_safe.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.Base64;


@Slf4j
@Service
@RequiredArgsConstructor
public class ImageValidationService {

    public boolean validateImage(String base64String) {
        boolean isValid;
        String JPG_MAGIC_NUMBERS = "FFD8FFE0";
        String PNG_MAGIC_NUMBERS = "89504E47";

        if (!base64String.contains(",")) return false;

        String[] base64 = base64String.split(",");

        try {
            byte[] decodedString = Base64.getDecoder().decode(base64[1].getBytes());
            String encodeHexString = DatatypeConverter.printHexBinary(decodedString);
            isValid = encodeHexString.startsWith(JPG_MAGIC_NUMBERS) || encodeHexString.startsWith(PNG_MAGIC_NUMBERS);
        } catch (IllegalArgumentException illegalArgumentException) {
            isValid = false;
        }
        return isValid;
    }
}
