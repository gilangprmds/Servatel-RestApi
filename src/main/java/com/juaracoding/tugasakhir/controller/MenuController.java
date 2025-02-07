package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.dto.validasi.ValMenuDTO;
import com.juaracoding.tugasakhir.service.impl.MenuServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 21:58
@Last Modified 07/02/2025 21:58
Version 1.0
*/
@RestController
@RequestMapping("/menu")
public class MenuController {

    @Autowired
    private MenuServiceImpl menuServiceImpl;

    @PostMapping("")
    //@PreAuthorize("hasAuthority('Menu')")
    public ResponseEntity<Object> save(@Valid @RequestBody ValMenuDTO menu, HttpServletRequest request){
        return menuServiceImpl.save(menuServiceImpl.convertToMenu(menu),request);
    }

    @PutMapping("/{id}")
    //@PreAuthorize("hasAuthority('Menu')")
    public ResponseEntity<Object> update(
            @PathVariable(value = "id") Long id,
            @Valid @RequestBody ValMenuDTO menu, HttpServletRequest request){
        return menuServiceImpl.update(id,menuServiceImpl.convertToMenu(menu),request);
    }

    @DeleteMapping("/{id}")
    //@PreAuthorize("hasAuthority('Menu')")
    public ResponseEntity<Object> delete(
            @PathVariable(value = "id") Long id,
            HttpServletRequest request){
        return menuServiceImpl.delete(id,request);
    }
}
