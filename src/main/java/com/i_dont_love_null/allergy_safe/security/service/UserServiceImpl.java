package com.i_dont_love_null.allergy_safe.security.service;

import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import com.i_dont_love_null.allergy_safe.service.UserValidationService;
import com.i_dont_love_null.allergy_safe.security.dto.AuthenticatedUserDto;
import com.i_dont_love_null.allergy_safe.security.dto.RegistrationRequest;
import com.i_dont_love_null.allergy_safe.security.dto.RegistrationResponse;
import com.i_dont_love_null.allergy_safe.security.mapper.UserMapper;
import com.i_dont_love_null.allergy_safe.utils.GeneralMessageAccessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


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
	public AuthenticatedUserDto findAuthenticatedUserByEmail(String email) {

		final User user = findByEmail(email);

		return UserMapper.INSTANCE.convertToAuthenticatedUserDto(user);
	}
}
