package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.FriendRequest;
import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.dto.ProfileRequest;
import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.security.dto.PasswordChangeRequest;
import com.i_dont_love_null.allergy_safe.security.dto.PasswordChangeResponse;
import com.i_dont_love_null.allergy_safe.security.dto.RegistrationRequest;
import com.i_dont_love_null.allergy_safe.security.dto.RegistrationResponse;
import com.i_dont_love_null.allergy_safe.security.service.UserDetailsServiceImpl;
import com.i_dont_love_null.allergy_safe.security.service.UserService;
import com.i_dont_love_null.allergy_safe.service.FriendService;
import com.i_dont_love_null.allergy_safe.service.ProfileService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Tag(name = "User", description = "사용자 관련 API")
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final UserDetailsServiceImpl userDetailsService;
    private final ProfileService profileService;
    private final FriendService friendService;

    @PostMapping
    public ResponseEntity<RegistrationResponse> registrationRequest(@Valid @RequestBody RegistrationRequest registrationRequest) {

        final RegistrationResponse registrationResponse = userService.registration(registrationRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(registrationResponse);
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validateEmailToken(@RequestParam("token") String emailToken) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.validateEmailToken(emailToken));
    }

    @PutMapping
    public ResponseEntity<PasswordChangeResponse> changePassword(@Valid @RequestBody PasswordChangeRequest passwordChangeRequest) {
        User currentUser = userDetailsService.loadCurrentUser();

        final PasswordChangeResponse passwordChangeResponse = userService.changePassword(currentUser, passwordChangeRequest);

        return ResponseEntity.status(HttpStatus.OK).body(passwordChangeResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.loadCurrentUser());
    }

    @PostMapping("/profile")
    public ResponseEntity<IdResponse> createProfile(@Valid @RequestBody ProfileRequest profileRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createProfile(
                userDetailsService.loadCurrentUser(), profileRequest
        ));
    }

    @DeleteMapping("/profile/{id}")
    public ResponseEntity<IdResponse> deleteProfile(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.deleteProfile(userDetailsService.loadCurrentUser(), id));
    }

    @PostMapping("/friend")
    public ResponseEntity<IdResponse> createFriend(@Valid @RequestBody FriendRequest friendRequest){
        return ResponseEntity.status(HttpStatus.CREATED).body(friendService.createFriend(
                userDetailsService.loadCurrentUser(), friendRequest
        ));
    }

    @DeleteMapping("/friend/{id}")
    public ResponseEntity<IdResponse> deleteFriend(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.deleteFriend(userDetailsService.loadCurrentUser(), id));
    }
}
