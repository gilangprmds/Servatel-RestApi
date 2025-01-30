package com.juaracoding.tugasakhir.dto.validasi;

import com.juaracoding.tugasakhir.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long id;
    private HotelRegistrationDTO hotel;
    private RoomType roomType;
    private Integer roomCount;
    private Double pricePerNight;
}
