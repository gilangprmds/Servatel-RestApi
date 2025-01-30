package com.juaracoding.tugasakhir.service;

import com.juaracoding.tugasakhir.model.Address;
import com.juaracoding.tugasakhir.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    public Address save(Address address) {
        return addressRepository.save(address);
    }
}
