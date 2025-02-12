package com.juaracoding.tugasakhir.repository;

import com.juaracoding.tugasakhir.model.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends JpaRepository<Menu,Long> {

    //DERIVED query
    //select * from MstGroupMenu WHERE NamaGroupMenu LIKE toLower('%?%')
    public Page<Menu> findByNameContainsIgnoreCase(Pageable pageable, String name);

    public Page<Menu> findByPathContainsIgnoreCase(Pageable pageable, String name);

    // UNTUK REPORT
    public List<Menu> findByNameContainsIgnoreCase(String name);
    public List<Menu> findByPathContainsIgnoreCase(String name);

//    @Query(value = "SELECT m FROM Menu m WHERE lower(m.groupMenu.namaGroupMenu) LIKE lower(concat('%',?1,'%'))")
//    public Page<Menu> cariGroupMenu(Pageable pageable, String nama);

//    @Query(value = "SELECT m FROM Menu m WHERE lower(m.groupMenu.namaGroupMenu) LIKE lower(concat('%',?1,'%'))")
//    public List<Menu> cariGroupMenu(String nama);

    public Optional<Menu> findTopByOrderByIdDesc();
}