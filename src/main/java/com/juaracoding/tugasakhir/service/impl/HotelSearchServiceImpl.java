package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.dto.respone.RespHotelAvailableDTO;
import com.juaracoding.tugasakhir.enums.RoomType;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Hotel;
import com.juaracoding.tugasakhir.repository.HotelRepository;
import com.juaracoding.tugasakhir.service.HotelSearchService;
import com.juaracoding.tugasakhir.util.LoggingFile;
import com.juaracoding.tugasakhir.util.TransformPagination;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class HotelSearchServiceImpl implements HotelSearchService {
    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private AvailabilityServiceImpl availabilityService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransformPagination transformPagination;

    @Override
    public ResponseEntity<Object> findAllAvailableHotel(Pageable pageable, String city, LocalDate checkinDate,
                                                      LocalDate checkoutDate, HttpServletRequest request) {
        Map<String,Object> mapList;
        List<RespHotelAvailableDTO> respHotelAvailableDTOS;
        try{
            if (checkinDate.isBefore(LocalDate.now())) {
                return new ResponseHandler().handleResponse("Tanggal check-in sudah lampau",
                        HttpStatus.BAD_REQUEST,null,"FVAUT02001", request);
            }
            if (checkoutDate.isBefore(checkinDate.plusDays(1))) {
                return new ResponseHandler().handleResponse("Tanggal check-out harus setelah tanggal check-in",
                        HttpStatus.BAD_REQUEST,null,"FVAUT02002", request);
            }

            // Number of days between check-in and check-out
            Long numberOfDays = ChronoUnit.DAYS.between(checkinDate, checkoutDate);

            // 1. Fetch hotels that satisfy the criteria (min 1 available room throughout the booking range)
            Page<Hotel> pageHotelWithAvailableRooms = hotelRepository.findHotelsWithAvailableRooms(pageable, city, checkinDate, checkoutDate, numberOfDays);
            List<Hotel> listhotelsWithAvailableRooms = pageHotelWithAvailableRooms.getContent();

            // 2. Fetch hotels that don't have any availability records for the entire booking range

            Page<Hotel> pagehotelsWithoutAvailabilityRecords = hotelRepository.findHotelsWithoutAvailabilityRecords(pageable, city, checkinDate, checkoutDate);
            List<Hotel> listhotelsWithoutAvailabilityRecords = pagehotelsWithoutAvailabilityRecords.getContent();

            // 3. Fetch hotels with partial availability; some days with records meeting the criteria and some days without any records
            Page<Hotel> pagehotelsWithPartialAvailabilityRecords = hotelRepository.findHotelsWithPartialAvailabilityRecords(pageable, city, checkinDate, checkoutDate, numberOfDays);
            List<Hotel> listhotelsWithPartialAvailabilityRecords = pagehotelsWithPartialAvailabilityRecords.getContent();

            // Combine and deduplicate the hotels using a Set
            Set<Hotel> combinedHotels = new HashSet<>(listhotelsWithAvailableRooms);
            combinedHotels.addAll(listhotelsWithoutAvailabilityRecords);
            combinedHotels.addAll(listhotelsWithPartialAvailabilityRecords);

            respHotelAvailableDTOS= combinedHotels.stream()
                    .map(hotel -> mapHotelToHotelAvailabilityDto(hotel, checkinDate, checkoutDate))
                    .toList();

            Page<RespHotelAvailableDTO> page = convertListToPage(respHotelAvailableDTOS, pageable);
            mapList = transformPagination.transformPagination(respHotelAvailableDTOS, page, "id", "");
        } catch (Exception e) {
            LoggingFile.logException("HotelSearchService", "findAllAvailableHotel",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Diakses",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT02001", request);
        }
        return new ResponseHandler().handleResponse("OK", HttpStatus.OK, mapList,null, request);
    }

    @Override
    public ResponseEntity<Object> findAvailableHotelById(Long hotelId, LocalDate checkinDate,
                                                         LocalDate checkoutDate, HttpServletRequest request) {
        RespHotelAvailableDTO respHotelAvailableDTO;
        try {
            Optional<Hotel> availableHotel = hotelRepository.findById(hotelId);
            if (!availableHotel.isPresent()) {
                return new ResponseHandler().handleResponse("Hotel Tidak Ditemukan",
                        HttpStatus.BAD_REQUEST,null,"FVAUT02003", request);
            }
            Hotel hotel = availableHotel.get();
            respHotelAvailableDTO = mapHotelToHotelAvailabilityDto(hotel, checkinDate, checkoutDate);
        } catch (Exception e) {
            LoggingFile.logException("HotelSearchService", "findAvailableHotelById",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Diakses",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT02002", request);
        }
        return new ResponseHandler().handleResponse("OK", HttpStatus.OK, respHotelAvailableDTO,null, request);
    }

    @Override
    public RespHotelAvailableDTO mapHotelToHotelAvailabilityDto(Hotel hotel, LocalDate checkinDate,
                                                                LocalDate checkoutDate) {
        RespHotelAvailableDTO respHotelAvailableDTO = modelMapper.map(hotel, RespHotelAvailableDTO.class);

        // For each room type, find the minimum available rooms across the date range
        int maxAvailableStandardRooms = hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == RoomType.STANDARD_ROOM)
                .mapToInt(room -> availabilityService.getMinAvailableRooms(room.getId(), checkinDate, checkoutDate))
                .max()
                .orElse(0); // Assume no single rooms if none match the filter
        respHotelAvailableDTO.setMaxAvailableStandardRooms(maxAvailableStandardRooms);

        int maxAvailableDeluxDoubleRooms = hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == RoomType.DELUX_DOUBLE)
                .mapToInt(room -> availabilityService.getMinAvailableRooms(room.getId(), checkinDate, checkoutDate))
                .max()
                .orElse(0); // Assume no double rooms if none match the filter
        respHotelAvailableDTO.setMaxAvailableDeluxeDoubleRooms(maxAvailableDeluxDoubleRooms);

        return respHotelAvailableDTO;
    }

    public <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), list.size());

        List<T> sublist = list.subList(start, end);
        return new PageImpl<>(sublist, pageable, list.size());
    }
}
