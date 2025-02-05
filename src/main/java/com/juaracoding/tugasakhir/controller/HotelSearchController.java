package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.service.HotelSearchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/search")
public class HotelSearchController {
    @Autowired
    private HotelSearchService hotelSearchService;

    @GetMapping("")
    public ResponseEntity<Object> findAllAvailableHotels(@RequestParam(value = "city") String city,
                                                         @RequestParam(value = "checkinDate") LocalDate checkinDate,
                                                         @RequestParam(value = "checkoutDate") LocalDate checkoutDate,
                                                         HttpServletRequest request) {
        Pageable pageable = PageRequest.of(0,2, Sort.by("id"));//asc
        return hotelSearchService.findAllAvailableHotel(pageable, city, checkinDate, checkoutDate, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findAvailableHotelById(@PathVariable (value = "id") Long id,
                                                         @RequestParam(value = "checkinDate") LocalDate checkinDate,
                                                         @RequestParam(value = "checkoutDate") LocalDate checkoutDate,
                                                         HttpServletRequest request){
        return hotelSearchService.findAvailableHotelById(id, checkinDate, checkoutDate, request);
    }
}
