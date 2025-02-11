package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByUsername(String value);
    public Optional<User> findByEmail(String value);
    public List<User> findByUsernameOrNoHpOrEmailAndIsRegistered(String value1, String value2, String value3, Boolean value4);
}
