package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.AlertRequest;
import com.i_dont_love_null.allergy_safe.dto.AlertResponse;
import com.i_dont_love_null.allergy_safe.dto.SimpleProfile;
import com.i_dont_love_null.allergy_safe.model.*;
import com.i_dont_love_null.allergy_safe.repository.AllergyRepository;
import com.i_dont_love_null.allergy_safe.repository.IngredientRepository;
import com.i_dont_love_null.allergy_safe.repository.MaterialRepository;
import com.i_dont_love_null.allergy_safe.repository.UserRepository;
import com.i_dont_love_null.allergy_safe.utils.EntityListToStringList;
import com.i_dont_love_null.allergy_safe.utils.FindIntersection;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AlertService {
    private final UserRepository userRepository;
    private final AllergyRepository allergyRepository;
    private final MaterialRepository materialRepository;
    private final IngredientRepository ingredientRepository;

    private final AlertResponse alertResponse;

    private final ProfileService profileService;
    private final FoodService foodService;
    private final MedicineService medicineService;

    private final EntityListToStringList entityListToStringList;

    public AlertResponse alert(boolean isFood, User user, AlertRequest alertRequest) {
        Food food = null;
        Medicine medicine = null;

        if (isFood) {
            food = foodService.getFoodById(alertRequest.getId());
        } else {
            medicine = medicineService.getMedicineById(alertRequest.getId());
        }


        List<Long> friendProfileIds = new ArrayList<>();

        for (Friend friend : user.getFriends()) {
            Long friendUserId = friend.getUserId();
            Optional<User> friendUserOptional = userRepository.findById(friendUserId);
            User friendUser;

            if (friendUserOptional.isPresent()) {
                friendUser = friendUserOptional.get();
                friendProfileIds.add(friendUser.getProfiles().get(0).getId());
            }
        }

        List<SimpleProfile> family = new ArrayList<>();
        List<SimpleProfile> friend = new ArrayList<>();

        for (Long profileId : alertRequest.getProfileIdList()) {
            Profile profile = profileService.getProfileById(profileId);
            SimpleProfile simpleProfile = new SimpleProfile();
            simpleProfile.setId(profileId);
            simpleProfile.setName(profile.getName());

            List<String> allergyNames = entityListToStringList.convertWithName(profile.getAllergies());
            List<String> materialNames = entityListToStringList.convertWithName(profile.getMaterials());
            List<String> ingredientNames = entityListToStringList.convertWithName(profile.getIngredients());

            if (isFood) {
                List<String> allergyNamesFromFood = entityListToStringList.convertWithName(food.getAllergies());
                simpleProfile.setAllergies(FindIntersection.find(allergyNames, allergyNamesFromFood));

                List<String> materialNamesFromFood = entityListToStringList.convertWithName(food.getMaterials());
                simpleProfile.setMaterials(FindIntersection.find(materialNames, materialNamesFromFood));
            } else {
                List<String> ingredientNamesFromMedicine = entityListToStringList.convertWithName(medicine.getIngredients());
                simpleProfile.setIngredients(FindIntersection.find(ingredientNames, ingredientNamesFromMedicine));
            }

            if (profileService.isFamily(user, profileId)) {
                family.add(simpleProfile);
            } else if (friendProfileIds.contains(profileId)) {
                friend.add(simpleProfile);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근할 수 없는 프로필입니다.");
            }
        }
        alertResponse.setFamily(family);
        alertResponse.setFriend(friend);

        return alertResponse;
    }
}
