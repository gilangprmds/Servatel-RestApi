package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface RoomRepository extends JpaRepository<Room, Long> {

    //DELETE FROM MstRoom r WHERE r.HotelId =?1;
    @Modifying
    void deleteAllByHotelId(Long hotelId);
}
