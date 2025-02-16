package com.juaracoding.tugasakhir.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TrxBooking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDBooking")
    private Long id;

    @Column(name = "ConfirmationNumber", unique = true, nullable = false)
    private String confirmationNumber;

    @CreationTimestamp
    @Column(name = "BookingDate")
    private LocalDateTime bookingDate;

    @ManyToOne
    @JoinColumn(name = "IDUser", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "IDHotel", nullable = false)
    private Hotel hotel;

    @Column(name = "CheckinDate", nullable = false)
    private LocalDate checkinDate;

    @Column(name = "CheckoutDate", nullable = false)
    private LocalDate checkoutDate;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private BookedRoom bookedRooms;

    @OneToOne(mappedBy = "booking")
    private Payment payment;

    @PrePersist
    protected void onCreate() {
        this.confirmationNumber = UUID.randomUUID().toString().substring(0, 8);
    }

}
