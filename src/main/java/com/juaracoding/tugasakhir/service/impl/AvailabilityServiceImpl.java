package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.model.Room;
import com.juaracoding.tugasakhir.repository.AvailabilityRepository;
import com.juaracoding.tugasakhir.repository.RoomRepository;
import com.juaracoding.tugasakhir.service.AvailabilityService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class AvailabilityServiceImpl implements AvailabilityService {
    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private RoomRepository roomRepository;


    public Integer getMinAvailableRooms(Long roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);

        // Fetch the minimum available rooms throughout the booking range for a room ID.
        return availabilityRepository.getMinAvailableRooms(roomId, checkinDate, checkoutDate)
                .orElse(room.getRoomCount()); // Consider no record as full availability
    }
}
