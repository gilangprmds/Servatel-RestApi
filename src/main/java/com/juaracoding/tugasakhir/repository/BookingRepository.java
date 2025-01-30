package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
