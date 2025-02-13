package com.juaracoding.tugasakhir.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HotelImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDImage")
    private Long id;

    @Column(name = "LinkImage")
    private String linkImage;

    @ManyToOne
    @JoinColumn(name = "IDHotel", nullable = false)
    private Hotel hotel;
}
