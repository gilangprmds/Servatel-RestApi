package com.juaracoding.tugasakhir.service.impl;

import com.juaracoding.tugasakhir.config.OtherConfig;
import com.juaracoding.tugasakhir.dto.respone.RespCityDTO;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.Address;
import com.juaracoding.tugasakhir.repository.AddressRepository;
import com.juaracoding.tugasakhir.util.LoggingFile;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        List<RespCityDTO> cityDTOList;
        try{
            cityDTOList = addressRepository.countAddressByCity(city);

        }
        catch(Exception e){
            LoggingFile.logException("AddressService", "findByCity",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Diakses",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT02003", request);
        }
        return new ResponseHandler().handleResponse("ok", HttpStatus.OK,cityDTOList,null,request);
    }

    public ResponseEntity<Object> findAllByCity(HttpServletRequest request) {
        List<RespCityDTO> cityDTOList;
        Pageable pageable = PageRequest.of(0, 5);
        try{
            cityDTOList = addressRepository.countAllAddressByCity(pageable);

        }
        catch(Exception e){
            LoggingFile.logException("AddressService", "findAllByCity",e, OtherConfig.getEnableLogFile());
            return new ResponseHandler().handleResponse("Data Gagal Diakses",
                    HttpStatus.INTERNAL_SERVER_ERROR,null,"FEAUT02004", request);
        }
        return new ResponseHandler().handleResponse("ok", HttpStatus.OK,cityDTOList,null,request);
    }
}
