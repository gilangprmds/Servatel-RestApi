package com.juaracoding.tugasakhir.model;

import com.juaracoding.tugasakhir.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "MstRoom")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDRoom")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDHotel", nullable = false)
    private Hotel hotel;

    @Enumerated(EnumType.STRING)
    @Column(name = "RoomType")
    private RoomType roomType;

    @Column(name = "RoomCount")
    private Integer roomCount;

    @Column(name = "PricePerNight")
    private Double pricePerNight;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "room")
    private List<Availability> availabilities = new ArrayList<>();
}
