package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    public Page<Role> findByRoleContainsIgnoreCase(Pageable pageable, String nama);
}
