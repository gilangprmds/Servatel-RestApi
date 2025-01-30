package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    Optional<Hotel> findByName(String name);


}
