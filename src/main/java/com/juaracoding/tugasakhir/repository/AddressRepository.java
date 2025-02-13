package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.dto.respone.RespCityDTO;
import com.juaracoding.tugasakhir.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

List<Address> findByCityContainsIgnoreCase(String city);

}
