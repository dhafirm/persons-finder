package com.dhafir.demo.data;

import jakarta.persistence.*;
import lombok.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Location {
    // id refers to Person::id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long referenceId;

    private Double latitude;
    private Double longitude;

    @OneToOne
    @MapsId
    @JoinColumn(name = "reference_id") // Referencing the primary key of Person table
    private Person person;
}
