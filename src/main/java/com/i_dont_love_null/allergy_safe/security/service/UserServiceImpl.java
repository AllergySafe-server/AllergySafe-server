package com.i_dont_love_null.allergy_safe.security.service;

import com.i_dont_love_null.allergy_safe.dto.MailRequest;
import com.i_dont_love_null.allergy_safe.model.Profile;
import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.properties.AppProperties;
import com.i_dont_love_null.allergy_safe.repository.ProfileRepository;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import com.i_dont_love_null.allergy_safe.security.dto.*;
import com.i_dont_love_null.allergy_safe.security.mapper.UserMapper;
import com.i_dont_love_null.allergy_safe.service.EmailService;
import com.i_dont_love_null.allergy_safe.service.UserValidationService;
import com.i_dont_love_null.allergy_safe.utils.GeneralMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.mail.MessagingException;
import java.util.Objects;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String REGISTRATION_SUCCESSFUL = "registration_successful";
    private static final String REGISTRATION_MAIL_SUBJECT = "registration_mail_subject";

    private final AppProperties appProperties;

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserValidationService userValidationService;

    private final GeneralMessageAccessor generalMessageAccessor;

    private final EmailService emailService;

    private final MailRequest mailRequest;

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

        User user = registrationRequest.toEntity();
        user = userRepository.save(user);

        Profile profile = Profile.builder()
                .name(registrationRequest.getName())
                .user(user)
                .build();
        profile = profileRepository.save(profile);

        final String email = user.getEmail();
        final String appName = appProperties.getAppName();
        final String appDomain = appProperties.getAppDomain();

        final String subject = generalMessageAccessor.getMessage(null, REGISTRATION_MAIL_SUBJECT, appName);
        final String content = "<a href='" + appDomain + "/api/user/validate?token=" + user.getEmailToken() + "'>여기</a>를 클릭해 이메일을 인증해주세요.";

        mailRequest.setReceiverEmail(email);
        mailRequest.setSubject(subject);
        mailRequest.setContent(content);
        mailRequest.setSenderName(appName);


        try {
            emailService.sendMail(mailRequest);
        } catch (MessagingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 전송에 실패했습니다. 이메일 계정이 휴면 상태인지 확인해 주세요.");
        }


        final String registrationSuccessMessage = generalMessageAccessor.getMessage(null, REGISTRATION_SUCCESSFUL, email);

        log.info("{} registered successfully!", email);

        return new RegistrationResponse(registrationSuccessMessage);
    }

    @Override
    public boolean validateEmailToken(String emailToken) {
        User user = userRepository.findByEmailToken(emailToken);

        if (Objects.isNull(user) || Objects.isNull(user.getEmailToken())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유효하지 않은 토큰입니다.");
        }

        if (user.getIsActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 이메일 인증이 완료되었습니다.");
        }

        user = user.toBuilder()
                .isActive(true)
                .build();
        userRepository.save(user);
        return true;
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
