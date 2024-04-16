package com.dhafir.demo.data;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location {
    // id refers to Person::personId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;

    @OneToOne
    @JoinColumn(name = "id") // Referencing the primary key of Person table
    private Person person;
}
