package com.i_dont_love_null.allergy_safe.model;

import com.i_dont_love_null.allergy_safe.dto.MedicineResponse;
import com.i_dont_love_null.allergy_safe.utils.Nameable;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity

public class Medicine implements Nameable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEDICINE_ID")
    private Long id;

    private String name;

    @Column(unique = true)
    private String barcode;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "MEDICINE_INGREDIENT",
            joinColumns = @JoinColumn(name = "MEDICINE_ID"),
            inverseJoinColumns = @JoinColumn(name = "INGREDIENT_ID"))
    private List<Ingredient> ingredients = new ArrayList<>();

    public MedicineResponse toMedicineResponse() {
        MedicineResponse medicineResponse = new MedicineResponse();
        medicineResponse.setId(id);
        medicineResponse.setName(name);
        medicineResponse.setBarcode(barcode);

        List<String> stringOnlyIngredients = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            stringOnlyIngredients.add(ingredient.getName());
        }
        medicineResponse.setIngredients(stringOnlyIngredients);

        return medicineResponse;
    }
}
