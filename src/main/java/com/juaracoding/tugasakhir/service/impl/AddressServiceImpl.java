package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Address;
import com.juaracoding.tugasakhir.repository.AddressRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl {
    @Autowired
    private AddressRepository addressRepository;

    public Address save(Address address) {
        return addressRepository.save(address);
    }

    public ResponseEntity<Object> findByCity(String city, HttpServletRequest request) {
        List<Address> addressList;
        try{
            addressList = addressRepository.findByCityContainsIgnoreCase(city);

        }
        catch(Exception e){
            throw new RuntimeException();
        }
        return new ResponseHandler().handleResponse("ok", HttpStatus.OK,addressList,null,request);
    }
}
