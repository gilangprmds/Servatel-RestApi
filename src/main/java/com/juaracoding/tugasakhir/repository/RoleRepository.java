package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface RoleRepository extends JpaRepository<Role,Long> {

    //DERIVED query
    //select * from MstRole WHERE NamaGroupMenu LIKE toLower('%?%')
    //public Page<Role> findByRoleTypeContainsIgnoreCase(Pageable pageable, String name);

    @Query(value = "SELECT r FROM Role r WHERE CAST(r.roleType as String) LIKE concat('%',?1,'%')")
    public Page<Role> searchRoleType(Pageable pageable, String name);

    // UNTUK REPORT  LIKE concat('%',?1,'%') "
    @Query(value = "SELECT r FROM Role r WHERE CAST(r.roleType as String) LIKE concat('%',?1,'%')")
    public List<Role> searchRoleType(String name);
}
