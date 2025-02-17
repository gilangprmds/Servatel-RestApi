package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.dto.respone.RespBookingDTO;
import com.juaracoding.tugasakhir.dto.validasi.AddressDTO;
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
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<Object> createBooking(BookingRequestDTO bookingRequestDTO, String username,
                                                HttpServletRequest request) {
        RespBookingDTO respBookingDTO;
        try {
            validateBookingDate(bookingRequestDTO, request);
            Optional<User> user = userRepository.findByUsername(username);
            if (!user.isPresent()) {
                return new ResponseHandler().handleResponse("ID Customer Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST, null, "FVAUT03003", request);
            }
            Optional<Hotel> hotel = hotelRepository.findById(bookingRequestDTO.getHotelId());
            if (!hotel.isPresent()) {
                return new ResponseHandler().handleResponse("ID Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST, null, "FVAUT03004", request);
            }

            Booking booking = mapBookingReqDtoToBookingModel(bookingRequestDTO, user.get(), hotel.get());
            Booking savedBooking = bookingRepository.save(booking);
            Payment payment = paymentService.createPendingPayment(savedBooking, bookingRequestDTO);
            savedBooking.setPayment(payment);
            bookingRepository.save(savedBooking);
            availabilityService.updateAvailabilities(bookingRequestDTO.getHotelId(),
                    bookingRequestDTO.getCheckinDate(), bookingRequestDTO.getCheckoutDate(),
                    bookingRequestDTO.getRoomSelections(), request);
            respBookingDTO = mapBookingModelToBookingDto(savedBooking);
        } catch (Exception e) {
            LoggingFile.logException("BookingService", "createBooking", e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Disimpan",
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FEAUT01001", request);
        }
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.CREATED, respBookingDTO, null, request);
    }

    public ResponseEntity<Object> findBookingsByCustomerUsername(String username, HttpServletRequest request) {
        List<RespBookingDTO> bookingDTOS;
        try {
            List<Booking> booking = bookingRepository.findAllByUserUsername(username);
            bookingDTOS = booking.stream()
                    .map(this::mapBookingModelToBookingDto)
                    .sorted(Comparator.comparing(RespBookingDTO::getCheckinDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.OK, bookingDTOS, null, request);
    }

    public ResponseEntity<Object> findBookingById(Long id, HttpServletRequest request) {
        RespBookingDTO respBookingDTO;
        try {
            Optional<Booking> booking = bookingRepository.findById(id);
            if (!booking.isPresent()) {
                return new ResponseHandler().handleResponse("ID Booking Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST, null, "FVAUT03004", request);
            }
            Booking Booking = booking.get();
            respBookingDTO = mapBookingModelToBookingDto(Booking);
        }catch (Exception e) {
            return new ResponseHandler().handleResponse("Data Tidak Ditemukan",
                    HttpStatus.INTERNAL_SERVER_ERROR, null, "FEAUT03001", request);
        }
        return new ResponseHandler().handleResponse("OK", HttpStatus.OK,
                respBookingDTO,null,request);
    }

    private ResponseEntity<Object> validateBookingDate(BookingRequestDTO bookingRequestDTO, HttpServletRequest request) {
        if (bookingRequestDTO.getCheckinDate().isBefore(LocalDate.now())) {
            return new ResponseHandler().handleResponse("Tanggal check-in sudah lampau",
                    HttpStatus.BAD_REQUEST, null, "FVAUT03001", request);
        }
        if (bookingRequestDTO.getCheckoutDate().isBefore(bookingRequestDTO.getCheckinDate().plusDays(1))) {
            return new ResponseHandler().handleResponse("Tanggal check-out harus setelah tanggal check-in",
                    HttpStatus.BAD_REQUEST, null, "FVAUT03002", request);
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


        BookedRoom bookedRoom = BookedRoom.builder()
                .booking(booking)
                .roomType(bookingRequestDTO.getRoomSelections().getRoomType())
                .count(bookingRequestDTO.getRoomSelections().getRoomCount())
                .build();
        booking.setBookedRooms(bookedRoom);

        return booking;
    }

    public RespBookingDTO mapBookingModelToBookingDto(Booking booking) {
        AddressDTO addressDto = AddressDTO.builder()
                .streetName(booking.getHotel().getAddress().getStreetName())
                .city(booking.getHotel().getAddress().getCity())
                .country(booking.getHotel().getAddress().getCountry())
                .build();

        RoomSelectionDTO roomSelections = RoomSelectionDTO.builder()
                .roomCount(booking.getBookedRooms().getCount())
                .roomType(booking.getBookedRooms().getRoomType())
                .build();


        User customerUser = booking.getUser();
        Long durationDays = ChronoUnit.DAYS.between(booking.getCheckinDate(), booking.getCheckoutDate());
        return RespBookingDTO.builder()
                .id(booking.getId())
                .confirmationNumber(booking.getConfirmationNumber())
                .bookingDate(booking.getBookingDate())
                .customerId(booking.getUser().getId())
                .hotelId(booking.getHotel().getId())
                .checkinDate(booking.getCheckinDate())
                .checkoutDate(booking.getCheckoutDate())
                .durationDays(durationDays)
                .roomSelections(roomSelections)
                .totalPrice(booking.getPayment().getTotalPrice())
                .hotelName(booking.getHotel().getName())
                .hotelAddress(addressDto)
                .customerName(customerUser.getFirstName() + " " + customerUser.getLastName())
                .customerEmail(customerUser.getEmail())
                .paymentStatus(booking.getPayment().getPaymentStatus())
                .paymentMethod(booking.getPayment().getPaymentMethod())
                .build();
    }
}
