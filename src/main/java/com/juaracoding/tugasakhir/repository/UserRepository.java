package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
