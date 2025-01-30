package com.juaracoding.tugasakhir.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoomType {
    STANDARD_ROOM(2),
    DELUX_DOUBLE(2),
    DELUX_TWIN(2),
    FAMILY_ROOM(4);

    private final int capacity;
//
//    RoomType(int capacity) {
//        this.capacity = capacity;
//    }
}
