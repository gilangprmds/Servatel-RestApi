package com.juaracoding.tugasakhir.dto.response;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 20:05
@Last Modified 07/02/2025 20:05
Version 1.0
*/
//import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.juaracoding.tugasakhir.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RespRoleDTO {
        private Long id;
        private RoleType role;
        @JsonProperty("lt-menu")
        private List<RespMenuDTO> ltMenu;
}
