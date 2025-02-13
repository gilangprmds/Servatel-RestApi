package com.juaracoding.tugasakhir.model;

import com.juaracoding.tugasakhir.enums.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TrxBookedRoom")
public class BookedRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDBookedRoom")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDBooking", nullable = false)
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name = "RoomType", nullable = false)
    private RoomType roomType;

    @Column(name = "Count", nullable = false)
    private int count;
}
