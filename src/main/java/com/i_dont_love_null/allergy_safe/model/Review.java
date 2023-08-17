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

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVIEW_ID")
    @JsonIgnore
    private Long id;

    private Integer star;

    private String content;

    @JsonIgnore
    private LocalDateTime datetime;

    @OneToOne(mappedBy = "review")
    @JsonIgnore
    private User user;
}
