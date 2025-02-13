package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.dto.validasi.RoomSelectionDTO;
import com.juaracoding.tugasakhir.enums.RoomType;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Availability;
import com.juaracoding.tugasakhir.model.Hotel;
import com.juaracoding.tugasakhir.model.Room;
import com.juaracoding.tugasakhir.repository.AvailabilityRepository;
import com.juaracoding.tugasakhir.repository.HotelRepository;
import com.juaracoding.tugasakhir.repository.RoomRepository;
import com.juaracoding.tugasakhir.service.AvailabilityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    @Autowired
    private AvailabilityRepository availabilityRepository;
    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private RoomRepository roomRepository;


    public Integer getMinAvailableRooms(Long roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);

        // Fetch the minimum available rooms throughout the booking range for a room ID.
        return availabilityRepository.getMinAvailableRooms(roomId, checkinDate, checkoutDate)
                .orElse(room.getRoomCount()); // Consider no record as full availability
    }

    public ResponseEntity<Object> updateAvailabilities(Long hotelId, LocalDate checkinDate, LocalDate checkoutDate, List<RoomSelectionDTO> roomSelections, HttpServletRequest request) {
        try {
            Optional<Hotel> hotel = hotelRepository.findById(hotelId);
            if (!hotel.isPresent()) {
                return new ResponseHandler().handleResponse("ID Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST, null, "FVAUT03004", request);
            }
            roomSelections = roomSelections.stream()
                    .filter(roomSelection -> roomSelection.getCount() > 0)
                    .collect(Collectors.toList());

            // Iterate through the room selections made by the user
            for (RoomSelectionDTO roomSelection : roomSelections) {
                RoomType roomType = roomSelection.getRoomType();
                int selectedCount = roomSelection.getCount();

                // Find the room by roomType for the given hotel
                Room room = hotel.get().getRooms().stream()
                        .filter(r -> r.getRoomType() == roomType)
                        .findFirst()
                        .orElseThrow(() -> new EntityNotFoundException("Room type not found"));

                // Iterate through the dates and update or create availability
                for (LocalDate date = checkinDate; date.isBefore(checkoutDate); date = date.plusDays(1)) {
                    final LocalDate currentDate = date; // Temporary final variable
                    Availability availability = availabilityRepository.findByRoomIdAndDate(room.getId(), date)
                            .orElseGet(() -> Availability.builder()
                                    .hotel(hotel.get())
                                    .date(currentDate)
                                    .room(room)
                                    .availableRooms(room.getRoomCount())
                                    .build());

                    // Reduce the available rooms by the selected count
                    int updatedAvailableRooms = availability.getAvailableRooms() - selectedCount;
                    if (updatedAvailableRooms < 0) {
                        throw new IllegalArgumentException("Selected rooms exceed available rooms for date: " + currentDate);
                    }
                    availability.setAvailableRooms(updatedAvailableRooms);

                    availabilityRepository.save(availability);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
      return null;
    }
}
