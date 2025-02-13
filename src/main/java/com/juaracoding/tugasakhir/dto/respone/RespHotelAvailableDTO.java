package com.juaracoding.tugasakhir.dto.respone;

import com.juaracoding.tugasakhir.dto.validasi.AddressDTO;
import com.juaracoding.tugasakhir.dto.validasi.RoomDTO;
import com.juaracoding.tugasakhir.model.HotelImage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class RespHotelAvailableDTO {
    private Long id;

    private String name;

    private AddressDTO address;

    private List<RespRoomDTO> rooms = new ArrayList<>();

    private Integer maxAvailableStandardRooms;

    private Integer maxAvailableDeluxeDoubleRooms;

    private Integer maxAvailableDeluxeTwinRooms;

    private Integer maxAvailableFamilyRooms;

    private List<HotelImageDTO> hotelImages;

}
