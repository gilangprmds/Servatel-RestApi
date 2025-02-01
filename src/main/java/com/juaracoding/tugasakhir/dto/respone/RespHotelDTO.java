package com.juaracoding.tugasakhir.dto.respone;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juaracoding.tugasakhir.dto.validasi.AddressDTO;
import com.juaracoding.tugasakhir.dto.validasi.RoomDTO;
import com.juaracoding.tugasakhir.model.Address;
import com.juaracoding.tugasakhir.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RespHotelDTO {
    private Long id;
    private String name;
    private AddressDTO address;
    private List<RespRoomDTO> rooms;
    private String description;
    @JsonProperty("manager-username")
    private String userUsername;
}
