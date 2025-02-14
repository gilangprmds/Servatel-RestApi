package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.dto.respone.RespHotelAvailableDTO;
import com.juaracoding.tugasakhir.dto.respone.RespRoomDTO;
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

import java.lang.reflect.Method;
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
                                                      LocalDate checkoutDate, Integer roomCount, HttpServletRequest request) {
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
            List<Hotel> listhotelsWithAvailableRooms = hotelRepository.findHotelsWithAvailableRooms(city, checkinDate, checkoutDate, numberOfDays);

            // 2. Fetch hotels that don't have any availability records for the entire booking range
            List<Hotel> listhotelsWithoutAvailabilityRecords = hotelRepository.findHotelsWithoutAvailabilityRecords(city, checkinDate, checkoutDate);

            // 3. Fetch hotels with partial availability; some days with records meeting the criteria and some days without any records
            List<Hotel> listhotelsWithPartialAvailabilityRecords = hotelRepository.findHotelsWithPartialAvailabilityRecords(city, checkinDate, checkoutDate, numberOfDays);

            // Combine and deduplicate the hotels using a Set
            Set<Hotel> combinedHotels = new HashSet<>(listhotelsWithAvailableRooms);
            combinedHotels.addAll(listhotelsWithoutAvailabilityRecords);
            combinedHotels.addAll(listhotelsWithPartialAvailabilityRecords);

            List<Hotel> combinedList = new ArrayList<>(combinedHotels);

            respHotelAvailableDTOS= combinedList.stream()
                    .map(hotel -> mapHotelToHotelAvailabilityDto(hotel, checkinDate, checkoutDate))
                    .toList();
            respHotelAvailableDTOS = respHotelAvailableDTOS.stream()
                    .map(hotel -> {
                        // Filter room yang maxAvailableRooms >= roomsToBook
                        List<RespRoomDTO> filteredRooms = hotel.getRooms().stream()
                                .filter(room -> room.getMaxAvailableRooms() >= roomCount)
                                .collect(Collectors.toList());

                        // Jika hotel masih punya kamar yang tersedia, simpan ke hasil
                        if (!filteredRooms.isEmpty()) {
                            hotel.setRooms(filteredRooms); // Update daftar kamar hotel
                            return hotel;
                        }
                        return null;
                    })
                    .filter(Objects::nonNull) // Hapus hotel yang tidak punya kamar tersedia
                    .collect(Collectors.toList());
            Page<RespHotelAvailableDTO> pageHotelnew = convertListToPagessss(respHotelAvailableDTOS, pageable);
            mapList = transformPagination.transformPagination(pageHotelnew.getContent(), pageHotelnew, "id", "");
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

        int maxAvailableDeluxDoubleRooms = hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == RoomType.DELUX_DOUBLE)
                .mapToInt(room -> availabilityService.getMinAvailableRooms(room.getId(), checkinDate, checkoutDate))
                .max()
                .orElse(0); // Assume no double rooms if none match the filter

        int maxAvailableDeluxTwinRooms = hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == RoomType.DELUX_TWIN)
                .mapToInt(room -> availabilityService.getMinAvailableRooms(room.getId(), checkinDate, checkoutDate))
                .max()
                .orElse(0); // Assume no double rooms if none match the filter

        int maxAvailableFamilyRooms = hotel.getRooms().stream()
                .filter(room -> room.getRoomType() == RoomType.FAMILY_ROOM)
                .mapToInt(room -> availabilityService.getMinAvailableRooms(room.getId(), checkinDate, checkoutDate))
                .max()
                .orElse(0); // Assume no double rooms if none match the filter

        for (RespRoomDTO roomDTO: respHotelAvailableDTO.getRooms()){
            switch (roomDTO.getRoomType()) {
                case STANDARD_ROOM:
                    roomDTO.setMaxAvailableRooms(maxAvailableStandardRooms);
                    roomDTO.setMaxGuest(RoomType.STANDARD_ROOM.getCapacity());
                    break;
                case DELUX_DOUBLE:
                    roomDTO.setMaxAvailableRooms(maxAvailableDeluxDoubleRooms);
                    roomDTO.setMaxGuest(RoomType.DELUX_DOUBLE.getCapacity());
                    break;
                case DELUX_TWIN:
                    roomDTO.setMaxAvailableRooms(maxAvailableDeluxTwinRooms);
                    roomDTO.setMaxGuest(RoomType.DELUX_TWIN.getCapacity());
                    break;
                case FAMILY_ROOM:
                    roomDTO.setMaxAvailableRooms(maxAvailableFamilyRooms);
                    roomDTO.setMaxGuest(RoomType.FAMILY_ROOM.getCapacity());
                    break;
            }
        }

        return respHotelAvailableDTO;
    }

    public <T> Page<T> convertListToPage(List<T> list, Pageable pageable) {
        // 1️⃣ Pastikan data sudah diurutkan sesuai dengan pageable sorting
        if (pageable.getSort().isSorted()) {
            Comparator<T> comparator = pageable.getSort().stream()
                    .map(order -> {
                        Comparator<T> c = (o1, o2) -> {
                            try {
                                Object value1 = o1.getClass().getDeclaredField(order.getProperty()).get(o1);
                                Object value2 = o2.getClass().getDeclaredField(order.getProperty()).get(o2);

                                Comparable<Object> comparable1 = (Comparable<Object>) value1;
                                Comparable<Object> comparable2 = (Comparable<Object>) value2;

                                return order.isAscending() ? comparable1.compareTo(comparable2) : comparable2.compareTo(comparable1);
                            } catch (Exception e) {
                                throw new RuntimeException("Sorting error", e);
                            }
                        };
                        return c;
                    })
                    .reduce(Comparator::thenComparing)
                    .orElse((o1, o2) -> 0);

            list.sort(comparator);
        }

        // 2️⃣ Hitung start dan end untuk pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        // 3️⃣ Ambil subset data sesuai pagination
        List<T> paginatedList = list.subList(start, end);

        // 4️⃣ Kembalikan sebagai PageImpl
        return new PageImpl<>(paginatedList, pageable, list.size());
    }
    public Page<Hotel> convertListToPages(List<Hotel> list, Pageable pageable) {
        // 1️⃣ Pastikan data sudah diurutkan sesuai dengan pageable sorting
        if (pageable.getSort().isSorted()) {
            Comparator<Hotel> comparator = pageable.getSort().stream()
                    .map(order -> {
                        Comparator<Hotel> c = (o1, o2) -> {
                            try {
                                Object value1 = o1.getClass().getDeclaredField(order.getProperty()).get(o1);
                                Object value2 = o2.getClass().getDeclaredField(order.getProperty()).get(o2);

                                Comparable<Object> comparable1 = (Comparable<Object>) value1;
                                Comparable<Object> comparable2 = (Comparable<Object>) value2;

                                return order.isAscending() ? comparable1.compareTo(comparable2) : comparable2.compareTo(comparable1);
                            } catch (Exception e) {
                                throw new RuntimeException("Sorting error", e);
                            }
                        };
                        return c;
                    })
                    .reduce(Comparator::thenComparing)
                    .orElse((o1, o2) -> 0);

            list.sort(comparator);
        }

        // 2️⃣ Hitung start dan end untuk pagination
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());

        // 3️⃣ Ambil subset data sesuai pagination
        List<Hotel> paginatedList = list.subList(start, end);

        // 4️⃣ Kembalikan sebagai PageImpl
        return new PageImpl<>(paginatedList, pageable, list.size());
    }

    public <T>Page<T> convertListToPagessss(List<T> list, Pageable pageable) {
        if (pageable.getSort().isSorted()) {
            Comparator<T> comparator = pageable.getSort().stream()
                    .map(order -> {
                        Comparator<T> c = (o1, o2) -> {
                            try {
                                // Buat nama getter method (misal: getId, getName, dll.)
                                String getterMethod = "get" + order.getProperty().substring(0, 1).toUpperCase() + order.getProperty().substring(1);
                                Method method = o1.getClass().getMethod(getterMethod);

                                Object value1 = method.invoke(o1);
                                Object value2 = method.invoke(o2);

                                Comparable<Object> comparable1 = (Comparable<Object>) value1;
                                Comparable<Object> comparable2 = (Comparable<Object>) value2;

                                return order.isAscending() ? comparable1.compareTo(comparable2) : comparable2.compareTo(comparable1);
                            } catch (Exception e) {
                                throw new RuntimeException("Sorting error", e);
                            }
                        };
                        return c;
                    })
                    .reduce(Comparator::thenComparing)
                    .orElse((o1, o2) -> 0);

            list.sort(comparator);
        }

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), list.size());
        List<T> paginatedList = list.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, list.size());
    }
}
