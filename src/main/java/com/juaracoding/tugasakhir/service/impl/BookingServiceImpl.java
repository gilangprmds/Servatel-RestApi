package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.dto.validasi.BookingRequestDTO;
import com.juaracoding.tugasakhir.dto.validasi.RoomSelectionDTO;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.*;
import com.juaracoding.tugasakhir.repository.BookingRepository;
import com.juaracoding.tugasakhir.repository.HotelRepository;
import com.juaracoding.tugasakhir.repository.UserRepository;
import com.juaracoding.tugasakhir.util.LoggingFile;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BookingServiceImpl {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private AvailabilityServiceImpl availabilityService;

    @Transactional
    public ResponseEntity<Object> createBooking(BookingRequestDTO bookingRequestDTO, Long userId, HttpServletRequest request) {
        try {
            validateBookingDate(bookingRequestDTO, request);
            Optional<User> user =userRepository.findById(userId);
            if(!user.isPresent()) {
                return new ResponseHandler().handleResponse("ID Customer Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT03003", request);
            }
            Optional<Hotel> hotel = hotelRepository.findById(bookingRequestDTO.getHotelId());
            if(!hotel.isPresent()) {
                return new ResponseHandler().handleResponse("ID Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT03004", request);
            }

            Booking booking = mapBookingReqDtoToBookingModel(bookingRequestDTO, user.get(), hotel.get());
            Booking savedBooking = bookingRepository.save(booking);
            Payment payment = paymentService.createPendingPayment(savedBooking, bookingRequestDTO);
            savedBooking.setPayment(payment);
            bookingRepository.save(savedBooking);
            availabilityService.updateAvailabilities(bookingRequestDTO.getHotelId(),
                    bookingRequestDTO.getCheckinDate(), bookingRequestDTO.getCheckoutDate(),
                    bookingRequestDTO.getRoomSelections(), request);

        } catch (Exception e) {
            LoggingFile.logException("BookingService", "createBooking",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Disimpan",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT01001", request);
        }
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.CREATED, null, null,request);
    }

    private ResponseEntity<Object> validateBookingDate(BookingRequestDTO bookingRequestDTO, HttpServletRequest request) {
        if (bookingRequestDTO.getCheckinDate().isBefore(LocalDate.now())) {
            return new ResponseHandler().handleResponse("Tanggal check-in sudah lampau",
                    HttpStatus.BAD_REQUEST,null,"FVAUT03001", request);
        }
        if (bookingRequestDTO.getCheckoutDate().isBefore(bookingRequestDTO.getCheckinDate().plusDays(1))) {
            return new ResponseHandler().handleResponse("Tanggal check-out harus setelah tanggal check-in",
                    HttpStatus.BAD_REQUEST,null,"FVAUT03002", request);
        }
        return null;
    }

    private Booking mapBookingReqDtoToBookingModel(BookingRequestDTO bookingRequestDTO, User user, Hotel hotel) {
        Booking booking = Booking.builder()
                .user(user)
                .hotel(hotel)
                .checkinDate(bookingRequestDTO.getCheckinDate())
                .checkoutDate(bookingRequestDTO.getCheckoutDate())
                .build();

        for (RoomSelectionDTO roomSelection : bookingRequestDTO.getRoomSelections()) {
            if (roomSelection.getCount() > 0) {
                BookedRoom bookedRoom = BookedRoom.builder()
                        .booking(booking)
                        .roomType(roomSelection.getRoomType())
                        .count(roomSelection.getCount())
                        .build();
                booking.getBookedRooms().add(bookedRoom);
            }
        }

        return booking;
    }
}
