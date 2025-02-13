package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.dto.validasi.ValUserDTO;
import com.juaracoding.tugasakhir.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

}
