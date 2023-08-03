package com.i_dont_love_null.allergy_safe.security.jwt;

import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import com.i_dont_love_null.allergy_safe.security.dto.AuthenticatedUserDto;
import com.i_dont_love_null.allergy_safe.security.dto.LoginRequest;
import com.i_dont_love_null.allergy_safe.security.dto.LoginResponse;
import com.i_dont_love_null.allergy_safe.security.mapper.UserMapper;
import com.i_dont_love_null.allergy_safe.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {

    private final UserService userService;

    private final JwtTokenManager jwtTokenManager;

    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    public LoginResponse getLoginResponse(LoginRequest loginRequest) {

        final String email = loginRequest.getEmail();
        final String password = loginRequest.getPassword();

        final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        final AuthenticatedUserDto authenticatedUserDto = userService.findAuthenticatedUserByEmail(email);

        final User user = UserMapper.INSTANCE.convertToUser(authenticatedUserDto);
        final String token = jwtTokenManager.generateToken(user);

        User userIsActive = userRepository.findByEmail(email);

        if (userIsActive == null || !userIsActive.isActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "인증되지 않았습니다.");
        }

        log.info("{} has successfully logged in!", user.getEmail());

        return new LoginResponse(token);
    }

}
