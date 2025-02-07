package com.juaracoding.tugasakhir.dto.validasi;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 22:04
@Last Modified 07/02/2025 22:04
Version 1.0
*/
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValMenuDTO {

    @NotNull(message = "Field Nama Tidak Boleh NULL")
    @NotEmpty(message = "Field Nama Tidak Boleh Kosong")
    @NotBlank(message = "Field Nama Tidak Boleh Blank")
    @Pattern(regexp = "^[\\w\\s]{6,40}$",message = "AflaNumerik Dengan Spasi Min 6 Max 40")
    private String name;

    @NotNull(message = "Field Path Tidak Boleh NULL")
    @NotEmpty(message = "Field Path Tidak Boleh Kosong")
    @NotBlank(message = "Field Path Tidak Boleh Blank")
    @Pattern(regexp = "^[\\w\\s/]{6,40}$",message = "AflaNumerik Dengan Spasi Min 6 Max 40")
    private String path;

}
