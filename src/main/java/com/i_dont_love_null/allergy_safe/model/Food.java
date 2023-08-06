package com.i_dont_love_null.allergy_safe.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.i_dont_love_null.allergy_safe.dto.FoodResponse;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FOOD_ID")
    private Long id;

    private String name;

    @Column(unique = true)
    private String barcode;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "FOOD_MATERIAL",
            joinColumns = @JoinColumn(name = "FOOD_ID"),
            inverseJoinColumns = @JoinColumn(name = "MATERIAL_ID"))
    private List<Material> materials = new ArrayList<>();


    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "FOOD_ALLERGY",
            joinColumns = @JoinColumn(name = "FOOD_ID"),
            inverseJoinColumns = @JoinColumn(name = "ALLERGY_ID"))
    private List<Allergy> allergies = new ArrayList<>();

    public FoodResponse toFoodResponse() {
        FoodResponse foodResponse = new FoodResponse();
        foodResponse.setId(id);
        foodResponse.setName(name);
        foodResponse.setBarcode(barcode);

        List<String> stringOnlyMaterials = new ArrayList<>();
        List<String> stringOnlyAllergies = new ArrayList<>();

        for (Material material : materials) {
            stringOnlyMaterials.add(material.getName());
        }
        foodResponse.setMaterials(stringOnlyMaterials);

        for (Allergy allergy : allergies) {
            stringOnlyAllergies.add(allergy.getName());
        }
        foodResponse.setAllergies(stringOnlyAllergies);


        return foodResponse;
    }
}
