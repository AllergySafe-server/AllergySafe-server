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
public class TakenMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAKENMEDICINE_ID")
    private Long id;

    private LocalDateTime datetime;

    @ManyToOne
    @JoinColumn(name = "MEDICINE_ID")
    private Medicine medicine;

    @ManyToOne
    @JoinColumn(name = "DIARY_ID")
    @JsonIgnore
    private Diary diary;
}
