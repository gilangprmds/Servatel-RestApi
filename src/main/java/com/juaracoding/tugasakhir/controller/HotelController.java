package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.dto.validasi.HotelRegistrationDTO;
import com.juaracoding.tugasakhir.service.HotelService;
import com.juaracoding.tugasakhir.service.impl.HotelServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
public class HotelController {
    @Autowired
    private HotelService hotelService;

    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody HotelRegistrationDTO hotelRegistrationDTO, HttpServletRequest request) {
        return hotelService.save(hotelService.mapHotelRegistrationDTOtoHotel(hotelRegistrationDTO),request);
    }
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
        Pageable pageable = PageRequest.of(0,3, Sort.by("id"));//asc
        return hotelService.findAll(pageable,request);
    }

    @GetMapping("/manager/hotels/{id}")
    public ResponseEntity<Object> findAllByManagerId( @PathVariable(value = "id") Long id,
            HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,2, Sort.by("id"));//asc
        return hotelService.findAllByManagerId(pageable, id, request);
    }

    @GetMapping("/hotel/{id}")
    public ResponseEntity<Object> findById( @PathVariable(value = "id") Long id,
                                                      HttpServletRequest request){
        return hotelService.findById(id, request);
    }
}
