package com.i_dont_love_null.allergy_safe.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.i_dont_love_null.allergy_safe.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@RequiredArgsConstructor
public class JwtTokenManager {

    private final JwtProperties jwtProperties;

    public String generateToken(User user) {

        final String email = user.getEmail();

        return JWT.create()
                .withSubject(email)
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMinute() * 60 * 1000))
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes()));
    }

    public String getEmailFromToken(String token) {

        final DecodedJWT decodedJWT = getDecodedJWT(token);

        return decodedJWT.getSubject();
    }

    public boolean validateToken(String token, String authenticatedEmail) {

        final String emailFromToken = getEmailFromToken(token);

        final boolean equalsEmail = emailFromToken.equals(authenticatedEmail);
        final boolean tokenExpired = isTokenExpired(token);

        return equalsEmail && !tokenExpired;
    }

    private boolean isTokenExpired(String token) {

        final Date expirationDateFromToken = getExpirationDateFromToken(token);
        return expirationDateFromToken.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {

        final DecodedJWT decodedJWT = getDecodedJWT(token);

        return decodedJWT.getExpiresAt();
    }

    private DecodedJWT getDecodedJWT(String token) {

        final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey().getBytes())).build();

        return jwtVerifier.verify(token);
    }

}
