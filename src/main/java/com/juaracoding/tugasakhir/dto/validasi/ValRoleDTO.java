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

    private RoleType role;
    @NotNull(message = "Menu Wajib Di Isi")
    @JsonProperty("lt-menu")
    private List<RespMenuDTO> ltMenu;




}
