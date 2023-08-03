package com.i_dont_love_null.allergy_safe.security.service;

import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import com.i_dont_love_null.allergy_safe.security.dto.*;
import com.i_dont_love_null.allergy_safe.security.mapper.UserMapper;
import com.i_dont_love_null.allergy_safe.service.UserValidationService;
import com.i_dont_love_null.allergy_safe.utils.GeneralMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String REGISTRATION_SUCCESSFUL = "registration_successful";

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserValidationService userValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    @Override
    public User findByEmail(String username) {

        return userRepository.findByEmail(username);
    }

    @Override
    public User findByPassword(String password) {

        return userRepository.findByPassword(password);
    }

    @Override
    public RegistrationResponse registration(RegistrationRequest registrationRequest) {

        userValidationService.validateUser(registrationRequest);

        final User user = registrationRequest.toEntity();
        userRepository.save(user);

        final String email = registrationRequest.getEmail();
        final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, email);

        log.info("{} registered successfully!", email);

        return new RegistrationResponse(registrationSuccessMessage);
    }

    @Override
    public String validateEmailToken(User user, String emailToken) {
        if (user.getEmailToken() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "토큰 없음");
        }

        user = user.toBuilder()
                .isActive(true)
                .build();
        userRepository.save(user);
        return "인증 완료";
    }

    @Override
    public AuthenticatedUserDto findAuthenticatedUserByEmail(String email) {

        final User user = findByEmail(email);

        return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
    }

    @Override
    public PasswordChangeResponse changePassword(User user, PasswordChangeRequest passwordChangeRequest) {
        if (!bCryptPasswordEncoder.matches(passwordChangeRequest.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "현재 비밀번호가 틀립니다.");
        }

        String encodedNewPassword = bCryptPasswordEncoder.encode(passwordChangeRequest.getNewPassword());
        user = user.toBuilder().
                password(encodedNewPassword)
                .build();
        userRepository.save(user);
        return new PasswordChangeResponse(user.getId());
    }

}
