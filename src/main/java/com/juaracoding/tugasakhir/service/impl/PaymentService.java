package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.dto.validasi.BookingRequestDTO;
import com.juaracoding.tugasakhir.enums.Currency;
import com.juaracoding.tugasakhir.enums.PaymentMethod;
import com.juaracoding.tugasakhir.enums.PaymentStatus;
import com.juaracoding.tugasakhir.model.Booking;
import com.juaracoding.tugasakhir.model.Payment;
import com.juaracoding.tugasakhir.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;

    @Transactional
    public Payment createPendingPayment(Booking booking, BookingRequestDTO bookingRequestDTO) {
        Payment payment = Payment.builder()
                .booking(booking)
                .paymentStatus(PaymentStatus.PENDING) // Set status PENDING saat reservasi dibuat
                .paymentMethod(null) // Biarkan null, karena user akan memilih nanti
                .totalPrice(bookingRequestDTO.getTotalPrice()) // Gunakan harga dari reservasi
                .currency(Currency.IDR) // Default ke IDR, bisa diubah sesuai kebutuhan
                .build();

        return paymentRepository.save(payment);
    }
}
