package com.i_dont_love_null.allergy_safe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "OccuredSymptom")
public class OccuredSymptom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OCCUREDSYMPTOM_ID")
    private Long id;

    private LocalDateTime datetime;

    @Lob
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "SYMPTOM_ID")
    private Symptom symptom;

    @ManyToOne
    @JoinColumn(name = "DIARY_ID")
    @JsonIgnore
    private Diary diary;
}
