package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.dto.respone.RespCityDTO;
import com.juaracoding.tugasakhir.model.Address;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

List<Address> findByCityContainsIgnoreCase(String city);

@Query(value = "SELECT new com.juaracoding.tugasakhir.dto.respone.RespCityDTO(a.city, COUNT(a))  FROM Address a WHERE lower(a.city) LIKE lower(concat('%',?1,'%'))  GROUP BY a.city")
List<RespCityDTO> countAddressByCity(String city);

@Query(value = "SELECT new com.juaracoding.tugasakhir.dto.respone.RespCityDTO(a.city, COUNT(a)) FROM Address a GROUP BY a.city ORDER BY COUNT(a) DESC")
List<RespCityDTO> countAllAddressByCity(Pageable pageable);
}
