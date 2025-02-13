package com.juaracoding.tugasakhir.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TrxAvailability")
public class Availability {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "AvailableRooms")
    private Integer availableRooms;

    @Column(name = "Date")
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "IDHotel")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "IDRoom")
    private Room room;

}
