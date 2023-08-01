package com.i_dont_love_null.allergy_safe.security.service;

import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.security.dto.AuthenticatedUserDto;
import com.i_dont_love_null.allergy_safe.security.dto.RegistrationRequest;
import com.i_dont_love_null.allergy_safe.security.dto.RegistrationResponse;


public interface UserService {

	User findByEmail(String email);

	RegistrationResponse registration(RegistrationRequest registrationRequest);

	AuthenticatedUserDto findAuthenticatedUserByEmail(String email);

}
