package com.juaracoding.tugasakhir.dto.table;

import com.juaracoding.tugasakhir.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 12/02/2025 16:57
@Last Modified 12/02/2025 16:57
Version 1.0
*/
@Setter
@Getter
public class TableUserDTO {

    private Long id;
    private String username;
//    private String password;
    private String email;
    private String address;
    private String noHp;
    private RoleType roleType;
    private String tanggalLahir;
    private String firstName;
    private String lastName;
    private Integer umur;


}