package com.juaracoding.tugasakhir.dto.validasi;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 20:25
@Last Modified 07/02/2025 20:25
Version 1.0
*/
import com.fasterxml.jackson.annotation.JsonProperty;
import com.juaracoding.tugasakhir.dto.response.RespMenuDTO;
import com.juaracoding.tugasakhir.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ValRoleDTO {

    @NotNull(message = "Field Nama Tidak Boleh NULL")
    @NotEmpty(message = "Field Nama Tidak Boleh Kosong")
    @NotBlank(message = "Field Nama Tidak Boleh Blank")
    @JsonProperty("role-type")
    private RoleType roleType;

//    @NotNull(message = "Menu Wajib DIISI")
//    @JsonProperty("lt-menu")
//    private List<RespMenuDTO> ltMenu;




}
