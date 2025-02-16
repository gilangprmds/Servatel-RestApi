package com.juaracoding.tugasakhir.controller;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 20:23
@Last Modified 07/02/2025 20:23
Version 1.0
*/

import com.juaracoding.tugasakhir.dto.validasi.ValRoleDTO;
import com.juaracoding.tugasakhir.service.impl.RoleServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleServiceImpl roleServiceImpl;

    Map<String,String> mapFilter = new HashMap<>();


    public RoleController() {
        filterColumnByMap();
    }

    @GetMapping("")
    //@PreAuthorize("hasAuthority('Role')") //untuk menjaga tiap api
    public ResponseEntity<Object> findAll(
            HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));//asc
        return roleServiceImpl.findAll(pageable,request);
    }


    @PostMapping("")
    //@PreAuthorize("hasAuthority('Role')")
    public ResponseEntity<Object> save(@Valid @RequestBody ValRoleDTO RoleDTO, HttpServletRequest request){
        return roleServiceImpl.save(roleServiceImpl.convertToRole(RoleDTO),request);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('Role')")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody ValRoleDTO RoleDTO, HttpServletRequest request){
        return roleServiceImpl.update(id, roleServiceImpl.convertToRole(RoleDTO),request);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('Role')")
    public ResponseEntity<Object> delete(
            @PathVariable(value = "id") Long id,
            HttpServletRequest request){
        return roleServiceImpl.delete(id,request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id,
                                           HttpServletRequest request){
        return roleServiceImpl.findById(id,request);
    }

    @GetMapping("/{sort}/{sortBy}/{page}")
    @PreAuthorize("hasAuthority('Akses')")
    public ResponseEntity<Object> findByParam(
            @PathVariable(value = "sort") String sort,
            @PathVariable(value = "sortBy") String sortBy,//name
            @PathVariable(value = "page") Integer page,
            @RequestParam(value = "size") Integer size,
            @RequestParam(value = "column") String column,
            @RequestParam(value = "value") String value,
            HttpServletRequest request){
        Pageable pageable = null;
        sortBy = mapFilter.get(sortBy)==null?"id":sortBy;
        if(sort.equals("asc")){
            pageable = PageRequest.of(page,size, Sort.by(sortBy));//asc
        }else {
            pageable = PageRequest.of(page,size, Sort.by(sortBy).descending());//asc
        }
        return roleServiceImpl.findByParam(pageable,column,value,request);
    }

    public void filterColumnByMap(){
        mapFilter.put("roleType","roleType");
    }
}
