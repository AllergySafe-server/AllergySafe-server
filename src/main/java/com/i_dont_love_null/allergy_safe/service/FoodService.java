package com.i_dont_love_null.allergy_safe.service;

import com.i_dont_love_null.allergy_safe.dto.FoodRequest;
import com.i_dont_love_null.allergy_safe.dto.FoodResponse;
import com.i_dont_love_null.allergy_safe.dto.IdResponse;
import com.i_dont_love_null.allergy_safe.model.Allergy;
import com.i_dont_love_null.allergy_safe.model.Food;
import com.i_dont_love_null.allergy_safe.model.Material;
import com.i_dont_love_null.allergy_safe.repository.AllergyRepository;
import com.i_dont_love_null.allergy_safe.repository.FoodRepository;
import com.i_dont_love_null.allergy_safe.repository.MaterialRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
public class FoodService {
    private final FoodRepository foodRepository;
    private final AllergyRepository allergyRepository;
    private final MaterialRepository materialRepository;

    private final IdResponse idResponse;

    public FoodResponse getFoodById(Long id) {
        Optional<Food> food = foodRepository.findById(id);

        if (food.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 식품입니다.");
        }

        return food.get().toFoodResponse();
    }

    public IdResponse createFood(FoodRequest foodRequest) {
        List<Allergy> knownAllergies = allergyRepository.findAll();
        List<String> allergiesFromMaterials = new ArrayList<>();

        for (String material : foodRequest.getMaterials()) {
            for (Allergy allergy : knownAllergies) {
                if (material.contains(allergy.getName())) {
                    allergiesFromMaterials.add(allergy.getName());
                }
            }
        }

        Set<String> set = new HashSet<>(foodRequest.getAllergies());
        set.addAll(allergiesFromMaterials);

        foodRequest.setAllergies(new ArrayList<>(set));

        List<Allergy> foundAllergies = new ArrayList<>();
        for (String allergyName : foodRequest.getAllergies()) {
            Allergy allergy = Optional.ofNullable(allergyRepository.findByName(allergyName)).orElseGet(() -> {
                Allergy newAllergy = new Allergy(null, allergyName);
                return allergyRepository.save(newAllergy);
            });
            foundAllergies.add(allergy);
        }

        List<Material> foundMaterials = new ArrayList<>();
        for (String materialName : foodRequest.getMaterials()) {
            Material material2 = Optional.ofNullable(materialRepository.findByName(materialName)).orElseGet(() -> {
                Material newMaterial = new Material(null, materialName);
                return materialRepository.save(newMaterial);
            });
            foundMaterials.add(material2);
        }

        Food newFood = Food.builder()
                .name(foodRequest.getName())
                .allergies(foundAllergies)
                .materials(foundMaterials)
                .build();
        newFood = foodRepository.save(newFood);

        idResponse.setId(newFood.getId());

        return idResponse;
    }

}
