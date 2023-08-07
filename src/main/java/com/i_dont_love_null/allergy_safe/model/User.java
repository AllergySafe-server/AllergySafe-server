package com.i_dont_love_null.allergy_safe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;


    @Column(unique = true)
    private String email;
    private String name;

    @Column(unique = true)
    @JsonIgnore
    private String password;

    private Boolean isActive;

    @Column(unique = true)
    @JsonIgnore
    private String emailToken;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Profile> profiles = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.REMOVE)
    @JsonIgnore
    @JoinTable(name = "USER_FRIEND",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "FRIEND_ID"))
    private List<Friend> friends = new ArrayList<>();
}
