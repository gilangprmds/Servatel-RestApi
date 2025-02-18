package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 18/02/2025 20:00
@Last Modified 18/02/2025 20:00
Version 1.0
*/
@RestController
@RequestMapping("/manager")
public class ManagerController {

    UserServiceImpl userServiceImpl;

    @GetMapping("")
    @PreAuthorize("hasAuthority('User')")
    public ResponseEntity<Object> findAll(HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,100, Sort.by("id"));//asc
        return userServiceImpl.findAll(pageable,request);
    }
}
