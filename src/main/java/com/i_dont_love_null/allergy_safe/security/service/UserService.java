package com.i_dont_love_null.allergy_safe.security.service;

import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.security.dto.*;


public interface UserService {

    User findByEmail(String email);

    User findByPassword(String password);

    RegistrationResponse registration(RegistrationRequest registrationRequest);

    boolean validateEmailToken(String emailToken);

    AuthenticatedUserDto findAuthenticatedUserByEmail(String email);

    PasswordChangeResponse changePassword(User user, PasswordChangeRequest passwordChangeRequest);

    void checkIfExists(Long userId);

    IdResponse deleteUser(User user);

    SendPasswordResetEmailResponse sendPasswordResetEmail(String email);

    PasswordResetResponse passwordReset(PasswordResetRequest passwordResetRequest);
}
