package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.dto.validasi.BookingRequestDTO;
import com.juaracoding.tugasakhir.model.Booking;
import com.juaracoding.tugasakhir.service.impl.BookingServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/booking")
public class BookingController {
    @Autowired
    private BookingServiceImpl bookingService;

    @PostMapping("/create")
    public ResponseEntity<Object> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO,
                                                HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return bookingService.createBooking(bookingRequestDTO, username,request);
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<Object> findAllBookingByCustomerUsername(HttpServletRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return bookingService.findBookingsByCustomerUsername(username, request);
    }

    @GetMapping("/my-booking/{id}")
    public ResponseEntity<Object> findBookingById(@PathVariable Long id, HttpServletRequest request) {
        return bookingService.findBookingById(id, request);
    }
}
