package com.juaracoding.tugasakhir.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MstHotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDHotel")
    private Long id;

    @Column(name = "NamaHotel")
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "IDAddress")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "IDManager")
    private User user;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms = new ArrayList<>();

    @Column(name = "Deskripsi")
    private String description;

}
