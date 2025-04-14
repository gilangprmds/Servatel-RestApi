package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.service.impl.AddressServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/address")
public class AddressController {
    @Autowired
    private AddressServiceImpl addressService;

    @GetMapping("/city")
    public ResponseEntity<Object> findCity(@RequestParam(value = "city", required = false)  String city,
                                           HttpServletRequest request){

        if (city != null && !city.isEmpty()) {
            return addressService.findByCity(city, request); // pencarian
        } else {
            return addressService.findAllByCity(request); // semua data
        }
    }

}
