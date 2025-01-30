package com.juaracoding.tugasakhir.service;

import com.juaracoding.tugasakhir.dto.validasi.HotelRegistrationDTO;
import com.juaracoding.tugasakhir.dto.validasi.RoomDTO;
import com.juaracoding.tugasakhir.model.Hotel;
import com.juaracoding.tugasakhir.model.Room;
import com.juaracoding.tugasakhir.repository.RoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    ModelMapper modelMapper;
////    public Room save(RoomDTO roomDTO) {
//////        log.info("Attempting to save a new room: {}", roomDTO);
////        roomRepository.save(roomDTO);
//////        log.info("Successfully saved room with ID: {}", room.getId());
////        return room;
//    }
    public void saveRoom(Room room, Hotel hotel) {
        if(room.getId() != null) {
            Optional<Room> existingRoom = roomRepository.findById(room.getId());
            if (existingRoom.isPresent()) {
                existingRoom.get().setRoomType(room.getRoomType());
                existingRoom.get().setRoomCount(room.getRoomCount());
                existingRoom.get().setPricePerNight(room.getPricePerNight());
            }
            roomRepository.save(existingRoom.get());
        }else {
            room.setHotel(hotel);
            roomRepository.save(room);
        }
    }

    public Room mapRoomDTOtoRoom(RoomDTO roomDTO) {
        return modelMapper.map(roomDTO, Room.class);
    }
}
