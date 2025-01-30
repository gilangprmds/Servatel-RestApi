package com.juaracoding.tugasakhir.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDBooking")
    private Long id;

    @Column(name = "BookingDate")
    private LocalDateTime bookingDate;

    @Column(name = "CheckInDate")
    private LocalDate checkInDate;

    @Column(name = "CheckOutDate")
    private LocalDate checkOutDate;

    @Column(name = "ConfirmationCode")
    private String confirmationCode;

    @ManyToOne
    @JoinColumn(name = "IDCustomer")
    private User user;

    @ManyToOne
    @JoinColumn(name = "IDRoom")
    private Room room;


}
