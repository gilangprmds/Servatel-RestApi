package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {


    public Page<User> findByAddressContainsIgnoreCase(Pageable pageable, String name);
    public Page<User> findByUsernameContainsIgnoreCase(Pageable pageable, String name);
    public Page<User> findByPasswordContainsIgnoreCase(Pageable pageable, String name);
    public Page<User> findByEmailContainsIgnoreCase(Pageable pageable, String name);
    public Page<User> findByNoHpContainsIgnoreCase(Pageable pageable, String name);
    public Page<User> findByFirstNameContainsIgnoreCase(Pageable pageable, String name);
    public Page<User> findByLastNameContainsIgnoreCase(Pageable pageable, String name);
    public Optional<User> findByUsername(String value);
    public Optional<User> findByEmail(String value);
    public Optional<User> findByOtp(String value);
    public Optional<User> findByPassword(String value);
    public List<User> findByUsernameOrNoHpOrEmailAndIsRegistered(String value1,String value2, String value3,Boolean value4);

    /** update when database migration */
    @Query(value = "SELECT  x FROM User x WHERE CAST(DATEDIFF(year ,x.tanggalLahir,CURRENT_TIMESTAMP)AS STRING) LIKE CONCAT('%',?1,'%') ")
    public Page<User> cariUmur(Pageable pageable, String value);
}
