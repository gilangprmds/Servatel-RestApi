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
//    @NotNull(message = "Field Nama Tidak Boleh NULL")
//    @NotEmpty(message = "Field Nama Tidak Boleh Kosong")
//    @NotBlank(message = "Field Nama Tidak Boleh Blank")
//    @Pattern(regexp = "^[a-zA-Z\\s]{2,40}$",message = "AflaNumerik Dengan Spasi Min 2 Max 40")
//    private String name;

    @NotNull(message = "Field Nama Tidak Boleh NULL")
    @NotEmpty(message = "Field Nama Tidak Boleh Kosong")
    @NotBlank(message = "Field Nama Tidak Boleh Blank")
    private RoleType role;

    @NotNull(message = "Menu Wajib DIISI")
    private List<RespMenuDTO> ltMenu;




}
