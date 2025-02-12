package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;


public interface RoleRepository extends JpaRepository<Role,Long> {

    //DERIVED query
    //select * from MstRole WHERE NamaGroupMenu LIKE toLower('%?%')
    public Page<Role> findByRoleType(Pageable pageable, String roleType);

    // UNTUK REPORT
    public List<Role> findByRoleTypeContainsIgnoreCase(String name);
}
