package com.juaracoding.tugasakhir.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MstAddress")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDAddress")
    private Long id;

    @Column(name = "StreetName")
    private String streetName;

    @Column(name = "City")
    private String city;

    @Column(name = "Country")
    private String country;



}
