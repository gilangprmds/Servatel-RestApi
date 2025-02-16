package com.juaracoding.tugasakhir.dto.validasi;

import com.juaracoding.tugasakhir.enums.RoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomSelectionDTO {

    private RoomType roomType;
    private Integer roomCount;
}
