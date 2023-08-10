package com.i_dont_love_null.allergy_safe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity

public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PROFILE_ID")
    private Long id;

    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "PROFILE_MATERIAL",
            joinColumns = @JoinColumn(name = "PROFILE_ID"),
            inverseJoinColumns = @JoinColumn(name = "MATERIAL_ID"))
    private List<Material> materials = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "PROFILE_ALLERGY",
            joinColumns = @JoinColumn(name = "PROFILE_ID"),
            inverseJoinColumns = @JoinColumn(name = "ALLERGY_ID"))
    private List<Allergy> allergies = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JoinTable(name = "PROFILE_INGREDIENT",
            joinColumns = @JoinColumn(name = "PROFILE_ID"),
            inverseJoinColumns = @JoinColumn(name = "INGREDIENT_ID"))
    private List<Ingredient> ingredients = new ArrayList<>();

    @OneToMany(mappedBy = "profile", cascade = CascadeType.REMOVE)
    private List<Diary> diaries;
}
