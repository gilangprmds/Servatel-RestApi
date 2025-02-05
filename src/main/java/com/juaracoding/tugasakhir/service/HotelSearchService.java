package com.juaracoding.tugasakhir.service;

import com.juaracoding.tugasakhir.dto.respone.RespHotelAvailableDTO;
import com.juaracoding.tugasakhir.model.Hotel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface HotelSearchService {
    public ResponseEntity<Object> findAllAvailableHotel(Pageable pageable, String city, LocalDate checkinDate, LocalDate checkoutDate, HttpServletRequest request);

    public ResponseEntity<Object> findAvailableHotelById(Long hotelId, LocalDate checkinDate, LocalDate checkoutDate, HttpServletRequest request);

    public RespHotelAvailableDTO mapHotelToHotelAvailabilityDto(Hotel hotel, LocalDate checkinDate, LocalDate checkoutDate);

    public <T> Page<T> convertListToPage(List<T> list, Pageable pageable);
}
