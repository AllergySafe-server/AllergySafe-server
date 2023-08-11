package com.i_dont_love_null.allergy_safe.controller;

import com.i_dont_love_null.allergy_safe.dto.*;
import com.i_dont_love_null.allergy_safe.model.Profile;
import com.i_dont_love_null.allergy_safe.model.User;
import com.i_dont_love_null.allergy_safe.security.dto.*;
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

    @GetMapping("/profile/me")
    public ResponseEntity<ProfileListResponse> getProfileList() {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getList(userDetailsService.loadCurrentUser()));
    }

    @PostMapping("/profile/family")
    public ResponseEntity<IdResponse> createProfile(@Valid @RequestBody ProfileRequest profileRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createProfile(
                userDetailsService.loadCurrentUser(), profileRequest
        ));
    }

    @DeleteMapping("/profile/family/{profileId}")
    public ResponseEntity<IdResponse> deleteProfile(@PathVariable Long profileId) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.deleteProfile(userDetailsService.loadCurrentUser(), profileId));
    }

    @PostMapping("/profile/element/{profileId}")
    public ResponseEntity<IdResponse> createElement(@PathVariable Long profileId, @Valid @RequestBody ProfileElementRequest profileElementRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.createElement(
                userDetailsService.loadCurrentUser(), profileId, profileElementRequest
        ));
    }

    @DeleteMapping("/profile/element/{profileId}")
    public ResponseEntity<IdResponse> deleteElement(@PathVariable Long profileId, @Valid @RequestBody ProfileElementRequest profileElementRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(profileService.deleteElement(
                userDetailsService.loadCurrentUser(), profileId, profileElementRequest
        ));
    }

    @GetMapping("/profile/share/{profileId}")
    public ResponseEntity<Profile> getProfileByIdFromShare(@PathVariable Long profileId, @RequestParam("token") String token) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfileByIdAndValidateByEmailToken(profileId, token));
    }

    @PostMapping("/friend")
    public ResponseEntity<IdResponse> createFriend(@Valid @RequestBody FriendRequest friendRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(friendService.createFriend(
                userDetailsService.loadCurrentUser(), friendRequest
        ));
    }

    @DeleteMapping("/friend/{profileId}")
    public ResponseEntity<IdResponse> deleteFriend(@PathVariable Long profileId) {
        return ResponseEntity.status(HttpStatus.OK).body(friendService.deleteFriend(userDetailsService.loadCurrentUser(), profileId));
    }

    @DeleteMapping("/me")
    public ResponseEntity<IdResponse> deleteUser() {
        User currentUser = userDetailsService.loadCurrentUser();
        final IdResponse idResponse = userService.deleteUser(currentUser);

        return ResponseEntity.status(HttpStatus.OK).body(idResponse);
    }
}
