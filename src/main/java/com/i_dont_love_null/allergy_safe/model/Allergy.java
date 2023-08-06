package com.i_dont_love_null.allergy_safe.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class Allergy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALLERGY_ID")
    private Long id;

    @Column(unique = true)
    private String name;

}
