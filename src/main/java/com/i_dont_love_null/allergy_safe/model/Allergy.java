package com.i_dont_love_null.allergy_safe.model;

import com.i_dont_love_null.allergy_safe.utils.Nameable;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity

public class Allergy implements Nameable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALLERGY_ID")
    private Long id;

    @Column(unique = true)
    private String name;

    private String imageUrl;
}
