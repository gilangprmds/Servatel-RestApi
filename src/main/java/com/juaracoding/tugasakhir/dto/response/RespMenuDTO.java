package com.juaracoding.tugasakhir.dto.response;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 20:08
@Last Modified 07/02/2025 20:08
Version 1.0
*/
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RespMenuDTO {
    private Long id;
    private String name;
    private String path;


}
