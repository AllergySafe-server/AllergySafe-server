package com.i_dont_love_null.allergy_safe.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DIARY_ID")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "PROFILE_ID")
    private Profile profile;
    @OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private List<IngestedFood> ingestedFoods = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private List<TakenMedicine> takenMedicines = new ArrayList<>();

    @OneToMany(mappedBy = "diary", cascade = CascadeType.REMOVE)
    private List<OccuredSymptom> occuredSymptoms = new ArrayList<>();
}
