package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.dto.validasi.BookingRequestDTO;
import com.juaracoding.tugasakhir.model.Booking;
import com.juaracoding.tugasakhir.service.impl.BookingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingServiceImpl bookingService;

    @PostMapping("/create")
    public ResponseEntity<Object> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO, HttpServletRequest request) {
        Long userId = 5L;
        return bookingService.createBooking(bookingRequestDTO, userId,request);
    }
}
