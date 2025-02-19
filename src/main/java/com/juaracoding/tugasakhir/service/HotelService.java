package com.juaracoding.tugasakhir.service;

import com.juaracoding.tugasakhir.dto.validasi.HotelRegistrationDTO;
import com.juaracoding.tugasakhir.model.Hotel;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService<T> {
    public ResponseEntity<Object> save(T t, List<MultipartFile> files, List<MultipartFile> roomsImages, HttpServletRequest request);//001-010
    public ResponseEntity<Object> update(Long id,T t, HttpServletRequest request);//011-020
    public ResponseEntity<Object> delete(Long id, HttpServletRequest request);//021-030
    public ResponseEntity<Object> findAll(Pageable pageable, HttpServletRequest request);//031-040
    public ResponseEntity<Object> findById(Long id, HttpServletRequest request);//041-050
    public ResponseEntity<Object> findByParam(Pageable pageable,String columnName, String value, HttpServletRequest request);//051-060
    public ResponseEntity<Object> findAllByManagerId(Pageable pageable, String username, HttpServletRequest request);
    public Hotel mapHotelRegistrationDTOtoHotel(HotelRegistrationDTO hotelRegistrationDTO);
}
