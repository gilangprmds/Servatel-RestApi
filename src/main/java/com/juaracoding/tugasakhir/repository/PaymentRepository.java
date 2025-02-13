package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
