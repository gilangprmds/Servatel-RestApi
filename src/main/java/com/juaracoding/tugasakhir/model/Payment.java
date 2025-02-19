package com.juaracoding.tugasakhir.model;

import com.juaracoding.tugasakhir.enums.Currency;
//import com.juaracoding.tugasakhir.enums.PaymentMethod;
import com.juaracoding.tugasakhir.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "TrxPayment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPayment")
    private Long id;

    @CreationTimestamp
    @Column(name = "PaymentDate")
    private LocalDateTime paymentDate;

    @OneToOne
    @JoinColumn(name = "IDBooking", nullable = false)
    private Booking booking;


    @Column(name = "TotalPrice", nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "PaymentStatus", nullable = false)
    private PaymentStatus paymentStatus;

//    @Enumerated(EnumType.STRING)
//    @Column(name = "PaymentMethod", nullable = true)
//    private PaymentMethod paymentMethod;
    @Column(name = "PaymentMethod")
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "Currency", nullable = false)
    private Currency currency;

    @Column(name = "Token")
    private String token;
}
