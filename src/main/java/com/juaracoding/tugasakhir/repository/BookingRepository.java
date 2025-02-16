package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByUserUsername(String userId);

}
