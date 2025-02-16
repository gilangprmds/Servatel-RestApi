package com.juaracoding.tugasakhir.dto.respone;

import com.juaracoding.tugasakhir.dto.validasi.AddressDTO;
import com.juaracoding.tugasakhir.dto.validasi.RoomSelectionDTO;
import com.juaracoding.tugasakhir.enums.PaymentMethod;
import com.juaracoding.tugasakhir.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RespBookingDTO {
    private Long id;
    private String confirmationNumber;
    private LocalDateTime bookingDate;
    private Long customerId;
    private Long hotelId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Long durationDays;
    private RoomSelectionDTO roomSelections;
    private BigDecimal totalPrice;
    private String hotelName;
    private AddressDTO hotelAddress;
    private String customerName;
    private String customerEmail;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
}
