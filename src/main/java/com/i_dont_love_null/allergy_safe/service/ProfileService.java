package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.*;
import com.i_dont_love_null.allergy_safe.model.*;
import com.i_dont_love_null.allergy_safe.repository.*;
import com.i_dont_love_null.allergy_safe.security.service.UserServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final AllergyRepository allergyRepository;
    private final MaterialRepository materialRepository;
    private final IngredientRepository ingredientRepository;
    private final IdResponse idResponse;
    private final UserServiceImpl userService;
    private final ProfileListResponse profileListResponse;

    public IdResponse createProfile(User user, ProfileRequest profileRequest) {
        idResponse.setId(profileRepository.save(
                Profile.builder()
                        .name(profileRequest.getName())
                        .user(user)
                        .build()
        ).getId());
        return idResponse;
    }

    public IdResponse deleteProfile(User user, Long profileId) {
        Profile profile = getProfileById(profileId);
        if (user.getProfiles().get(0).getId().equals(profileId))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "자신의 프로필은 삭제할 수 없습니다.");

        checkIfFamily(user, profileId);
        profileRepository.save(profile.toBuilder()
                .allergies(null)
                .materials(null)
                .ingredients(null)
                .build());
        profileRepository.deleteById(profileId);
        idResponse.setId(profileId);

        return idResponse;
    }

    public void checkIfExists(Long profileId) {
        if (profileRepository.findById(profileId).isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");
    }


    public void checkIfFamily(User user, Long profileId) {
        checkIfExists(profileId);
        if (Objects.isNull(profileRepository.findByIdAndUser(profileId, user)))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근할 수 없는 프로필입니다.");
    }

    public boolean isFamily(User user, Long profileId) {
        checkIfExists(profileId);
        return Objects.nonNull(profileRepository.findByIdAndUser(profileId, user));
    }

    public IdResponse createElement(User user, Long profileId, ProfileElementRequest profileElementRequest) {
        checkIfFamily(user, profileId);

        Long elementId = profileElementRequest.getId();
        Optional<Profile> profileOptional = profileRepository.findById(profileId);
        Profile profile;

        if (profileOptional.isPresent()) profile = profileOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        List<Allergy> allergies = profile.getAllergies();
        List<Material> materials = profile.getMaterials();
        List<Ingredient> ingredients = profile.getIngredients();

        switch (profileElementRequest.getProfileElementType()) {
            case ALLERGY -> {
                Optional<Allergy> allergyOptional = allergyRepository.findById(elementId);
                Allergy allergy;

                if (allergyOptional.isPresent()) allergy = allergyOptional.get();
                else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 알러지 항원입니다.");

                for (Allergy allergy1 : allergies) {
                    if (allergy1.getId().equals(elementId))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 프로필에 등록된 알러지 항원입니다.");
                }

                allergies.add(allergy);
            }

            case MATERIAL -> {
                Optional<Material> materialOptional = materialRepository.findById(elementId);
                Material material;

                if (materialOptional.isPresent()) material = materialOptional.get();
                else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 식품 원재료입니다.");

                for (Material material1 : materials) {
                    if (material1.getId().equals(elementId))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 프로필에 등록된 식품 원재료입니다.");
                }

                materials.add(material);
            }

            case INGREDIENT -> {
                Optional<Ingredient> ingredientOptional = ingredientRepository.findById(elementId);
                Ingredient ingredient;

                if (ingredientOptional.isPresent()) ingredient = ingredientOptional.get();
                else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 의약품 성분입니다.");

                for (Ingredient ingredient1 : ingredients) {
                    if (ingredient1.getId().equals(elementId))
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 프로필에 등록된 의약품 성분입니다.");
                }

                ingredients.add(ingredient);
            }
        }

        profileRepository.save(
                profile.toBuilder()
                        .allergies(allergies)
                        .materials(materials)
                        .ingredients(ingredients)
                        .build()
        );

        idResponse.setId(profileId);
        return idResponse;
    }

    public IdResponse deleteElement(User user, Long profileId, ProfileElementRequest profileElementRequest) {
        checkIfFamily(user, profileId);

        Long elementId = profileElementRequest.getId();
        Optional<Profile> profileOptional = profileRepository.findById(profileId);
        Profile profile;

        if (profileOptional.isPresent()) profile = profileOptional.get();
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");

        List<Allergy> allergies = profile.getAllergies();
        List<Material> materials = profile.getMaterials();
        List<Ingredient> ingredients = profile.getIngredients();

        switch (profileElementRequest.getProfileElementType()) {
            case ALLERGY -> {
                List<Allergy> newAllergies = new ArrayList<>();

                for (Allergy allergy1 : allergies) {
                    if (!allergy1.getId().equals(elementId)) newAllergies.add(allergy1);
                }

                if (allergies.size() == newAllergies.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필에 등록되지 않은 알러지 항원입니다.");

                allergies = newAllergies;
            }

            case MATERIAL -> {
                List<Material> newMaterials = new ArrayList<>();

                for (Material material1 : materials) {
                    if (!material1.getId().equals(elementId)) newMaterials.add(material1);
                }

                if (materials.size() == newMaterials.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필에 등록되지 않은 식품 원재료입니다.");

                materials = newMaterials;
            }

            case INGREDIENT -> {
                List<Ingredient> newIngredients = new ArrayList<>();

                for (Ingredient ingredient1 : ingredients) {
                    if (!ingredient1.getId().equals(elementId)) newIngredients.add(ingredient1);
                }

                if (ingredients.size() == newIngredients.size())
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "프로필에 등록되지 않은 의약품 성분입니다.");

                ingredients = newIngredients;
            }
        }

        profileRepository.save(
                profile.toBuilder()
                        .allergies(allergies)
                        .materials(materials)
                        .ingredients(ingredients)
                        .build()
        );

        idResponse.setId(profileId);
        return idResponse;
    }

    public ProfileListResponse getList(User user) {
        List<Profile> profiles = new ArrayList<>();

        for (Friend friend : user.getFriends()) {
            Optional<User> userOptional = userRepository.findById(friend.getUserId());
            if (userOptional.isEmpty()) return profileListResponse;

            User foundUser = userOptional.get();
            profiles.add(foundUser.getProfiles().get(0));
        }

        profileListResponse.setFamily(user.getProfiles());
        profileListResponse.setFriend(profiles);

        return profileListResponse;
    }

    public Profile getProfileByIdAndValidateByEmailToken(Long profileId, String token) {
        User user = userRepository.findByEmailToken(token);
        if (Objects.isNull(user)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "유효하지 않은 토큰입니다.");

        Profile profile = null;

        for (Profile foundProfile : user.getProfiles()) {
            if (foundProfile.getId().equals(profileId)) profile = foundProfile;
        }
        if (Objects.isNull(profile))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "프로필이 존재하지 않거나 토큰과 일치하지 않습니다.");

        return profile;
    }

    public Profile getProfileById(Long profileId) {
        Optional<Profile> profileOptional = profileRepository.findById(profileId);

        if (profileOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 프로필입니다.");
        }

        return profileOptional.get();
    }

    public IdResponse addProfileImage(User user, Long profileId, ProfileImageUrlRequest profileImageUrlRequest) {
        Profile profile = getProfileById(profileId);

        checkIfFamily(user, profileId);

        profile = profile.toBuilder()
                .imageUrl(profileImageUrlRequest.getImageUrl())
                .build();

        profileRepository.save(profile);

        idResponse.setId(profileId);

        return idResponse;
    }

    public IdResponse deleteProfileImage(User user, Long profileId) {
        Profile profile = getProfileById(profileId);

        checkIfFamily(user, profileId);

        profile = profile.toBuilder()
                .imageUrl(null)
                .build();
        profileRepository.save(profile);
        idResponse.setId(profileId);

        return idResponse;


    }
}
