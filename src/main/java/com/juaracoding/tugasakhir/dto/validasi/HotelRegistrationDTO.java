package com.juaracoding.tugasakhir.dto.validasi;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRegistrationDTO {

    private String name;

    private AddressDTO address;

    private List<RoomDTO> rooms = new ArrayList<>();

    private String description;

    private List<MultipartFile> hotelImages;
}
