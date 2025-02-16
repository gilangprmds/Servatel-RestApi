package com.juaracoding.tugasakhir.controller;

import com.juaracoding.tugasakhir.dto.validasi.ValMenuDTO;
import com.juaracoding.tugasakhir.service.impl.MenuServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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

    Map<String,String> mapFilter = new HashMap<>();

    @GetMapping("")
    public ResponseEntity<Object> findAll(
            HttpServletRequest request){
        Pageable pageable = PageRequest.of(0,10, Sort.by("id"));//asc
        return menuServiceImpl.findAll(pageable,request);
    }

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

    @GetMapping("/{id}")
    public ResponseEntity<Object> findById(@PathVariable(value = "id") Long id,
                                           HttpServletRequest request){
        return menuServiceImpl.findById(id,request);
    }

    @GetMapping("/{sort}/{sortBy}/{page}")
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
        return menuServiceImpl.findByParam(pageable,column,value,request);
    }

    public void filterColumnByMap(){
        mapFilter.put("name","name");
        mapFilter.put("path","path");
    }
}
