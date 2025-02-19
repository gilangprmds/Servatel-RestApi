package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.dto.validasi.ValChangePasswordDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValOtpDTO;
import com.juaracoding.tugasakhir.dto.validasi.ValSetChangePasswordDTO;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import com.juaracoding.tugasakhir.model.User;
import com.juaracoding.tugasakhir.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 17/02/2025 11:53
@Last Modified 17/02/2025 11:53
Version 1.0
*/
@RestController
@RequestMapping("/pwd")
public class SetPasswordController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @GetMapping("/new-otp")
    public ResponseEntity<Object> newOtp(HttpServletRequest request){
        return userServiceImpl.newOtp(request);
    }

    @PostMapping("/set-change-password")
    public ResponseEntity<Object> setChangePassword(@Valid @RequestBody ValSetChangePasswordDTO valSetChangePasswordDTO,
                                                 HttpServletRequest request){
        return userServiceImpl.setChangePassword(valSetChangePasswordDTO, request);
    }
}
