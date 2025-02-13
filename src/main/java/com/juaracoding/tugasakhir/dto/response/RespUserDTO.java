package com.juaracoding.tugasakhir.dto.response;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 12/02/2025 17:01
@Last Modified 12/02/2025 17:01
Version 1.0
*/
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
public class RespUserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String address;
    private String noHp;
    private String firstName;
    private String lastName;
    private LocalDate tanggalLahir;
    private RespRoleDTO role;

}