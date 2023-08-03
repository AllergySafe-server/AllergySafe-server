package com.i_dont_love_null.allergy_safe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
@Getter
@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(unique = true)
    private String email;
    private String name;

    @Column(unique = true)
    @JsonIgnore
    private String password;

    @Column(nullable = false)
    private boolean isActive;

    @Column(unique = true)
    private String emailToken;


}
