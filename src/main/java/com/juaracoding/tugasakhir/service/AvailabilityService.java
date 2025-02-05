package com.juaracoding.tugasakhir.service;

import java.time.LocalDate;

public interface AvailabilityService {
    public Integer getMinAvailableRooms(Long roomId, LocalDate checkinDate, LocalDate checkoutDate);
}
