package com.juaracoding.tugasakhir.controller;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 10/02/2025 17:40
@Last Modified 10/02/2025 17:40
Version 1.0
*/
import com.juaracoding.tugasakhir.dto.validasi.ValLoginDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValRegisDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValVerifyRegisDTO;
import com.juaracoding.tugasakhir.service.impl.AppUserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {
    /**
     * LOGIN
     * REGISTRASI
     * FORGOT PASSWORD
     */

    @Autowired
    AppUserDetailService appUserDetailService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody ValLoginDTO valLoginDTO,
                                        HttpServletRequest request) {
        return appUserDetailService.login(appUserDetailService.convertToUser(valLoginDTO),request);
    }

    @PostMapping("/regis")
    public ResponseEntity<Object> register(@Valid @RequestBody ValRegisDTO regisDTO, HttpServletRequest request){
        return appUserDetailService.regis(appUserDetailService.convertToUser(regisDTO),request);
    }

    @PostMapping("/verify-regis")
    public ResponseEntity<Object> verifyRegister(@Valid @RequestBody ValVerifyRegisDTO valVerifyRegisDTO, HttpServletRequest request){
        return appUserDetailService.verifyRegis(appUserDetailService.convertToUser(valVerifyRegisDTO),request);
    }

//    /** ini buat coret-coretan aja */
//    @GetMapping("/coba")
//    public ResponseEntity<Object> responseDoank(){
//        ReportDTO reportDTO = new ReportDTO();
//        List<ValLoginDTO> lt = new ArrayList<>();
//
//        ValLoginDTO valLoginDTO = new ValLoginDTO();
//        valLoginDTO.setUsername("admin");
//        valLoginDTO.setPassword("Admin@123");
//        lt.add(valLoginDTO);
//
//        valLoginDTO = new ValLoginDTO();
//        valLoginDTO.setUsername("admin");
//        valLoginDTO.setPassword("Admin@1234");
//        lt.add(valLoginDTO);
//
//        valLoginDTO = new ValLoginDTO();
//        valLoginDTO.setUsername("admin");
//        valLoginDTO.setPassword("Admin@12345");
//        lt.add(valLoginDTO);
//        reportDTO.setList(lt);
//        reportDTO.setInfo("Informasi Tambahan!!");
//
//        return ResponseEntity.status(HttpStatus.OK).body(reportDTO);
//    }

}