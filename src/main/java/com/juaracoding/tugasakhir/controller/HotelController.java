package com.juaracoding.tugasakhir.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.tugasakhir.dto.validasi.HotelRegistrationDTO;
import com.juaracoding.tugasakhir.dto.validasi.RoomDTO;
import com.juaracoding.tugasakhir.model.Room;
import com.juaracoding.tugasakhir.service.HotelService;
import com.juaracoding.tugasakhir.service.impl.HotelServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> save(@RequestPart("hotel") String hotelJson,
                                       @RequestPart(value = "hotelImages") List<MultipartFile> hotelImages,
                                       HttpServletRequest request) {
        HotelRegistrationDTO hotelRegistrationDTO;
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            hotelRegistrationDTO = objectMapper.readValue(hotelJson, HotelRegistrationDTO.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid data format");
        }
        return hotelService.save(hotelService.mapHotelRegistrationDTOtoHotel(hotelRegistrationDTO), hotelImages, request);
    }


//    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<Object> save(@RequestPart("name") String name,
////                                       @RequestPart("streetName") String streetName,
////                                       @RequestPart("city") String city,
////                                       @RequestPart("country") String country,
//                                       @RequestPart("description") String description,
//                                       @RequestPart("hotelImages") List<MultipartFile> hotelImages,
////                                       @RequestPart("rooms") List<RoomDTO> rooms,
//                                       HttpServletRequest request) {
//        HotelRegistrationDTO hotelRegistrationDTO;
//        try{
//            ObjectMapper objectMapper = new ObjectMapper();
////            hotelRegistrationDTO = objectMapper.readValue(hotelJson, HotelRegistrationDTO.class);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Invalid data format");
//        }
////        return hotelService.save(hotelService.mapHotelRegistrationDTOtoHotel(hotelRegistrationDTO), file, request);
//        return null;
//    }






//    @GetMapping("/update/{id}")
//    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
//                                         HttpServletRequest request) {
//        return hotelService.update(id,hotelService.mapHotelRegistrationDTOtoHotel(hotelRegistrationDTO), request);
//    }
    @PutMapping("/update/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") Long id,
                                         @RequestBody HotelRegistrationDTO hotelRegistrationDTO, HttpServletRequest request) {
        return hotelService.update(id, hotelService.mapHotelRegistrationDTOtoHotel(hotelRegistrationDTO), request);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id,
                                         HttpServletRequest request) {
        return hotelService.delete(id, request);
    }
    @GetMapping("/hotels")
    public ResponseEntity<Object> findAll(
            HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,2, Sort.by("id"));//asc
        return hotelService.findAll(pageable,request);
    }

    @GetMapping("/manager/hotels/{id}")
    public ResponseEntity<Object> findAllByManagerId( @RequestParam(value = "page") Integer page,
                                                      @PathVariable(value = "id") Long id,
            HttpServletRequest request){
        Pageable pageable = PageRequest.of(page-1,2, Sort.by("id"));//asc
        return hotelService.findAllByManagerId(pageable, id, request);
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<Object> findById( @PathVariable(value = "id") Long id,
                                                      HttpServletRequest request){
        return hotelService.findById(id, request);
    }
}
