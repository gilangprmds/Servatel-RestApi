package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.dto.validasi.ValUserDTO;
import com.juaracoding.tugasakhir.service.impl.UserServiceImpl;
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

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 06/02/2025 17:38
@Last Modified 06/02/2025 17:38
Version 1.0
*/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    Map<String,String> mapFilter = new HashMap<>();

    public UserController() {
        filterColumnByMap();
    }

    @GetMapping("")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<Object> findAll(HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,100, Sort.by("id"));//asc
        return userServiceImpl.findAll(pageable,request);
    }

    @PostMapping("")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<Object> save(@Valid @RequestBody ValUserDTO userDTO, HttpServletRequest request){
        return userServiceImpl.save(userServiceImpl.convertToUser(userDTO),request);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody ValUserDTO userDTO, HttpServletRequest request){
        return userServiceImpl.update(id, userServiceImpl.convertToUser(userDTO),request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<Object> delete(
            @PathVariable(value = "id") Long id,
            HttpServletRequest request){
        return userServiceImpl.delete(id,request);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id,
                                           HttpServletRequest request){
        //tambahkan pageable
        Pageable pageable = PageRequest.of(0,5, Sort.by("id"));//asc

        return userServiceImpl.findById(id,request);
    }

    @GetMapping("/{sort}/{sortBy}/{page}")
    @PreAuthorize("hasAuthority('User')")
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
        return userServiceImpl.findByParam(pageable,column,value,request);
    }

    public void filterColumnByMap(){
        mapFilter.put("firstName","firstName");
        mapFilter.put("lastName","lastName");
        mapFilter.put("username","username");
        mapFilter.put("umur","umur");
        mapFilter.put("address","address");
        mapFilter.put("email","email");
        //mapFilter.put("password","password");
    }

}
