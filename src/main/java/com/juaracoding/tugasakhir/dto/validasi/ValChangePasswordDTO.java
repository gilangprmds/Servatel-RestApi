package com.juaracoding.tugasakhir.dto.validasi;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 14/02/2025 19:17
@Last Modified 14/02/2025 19:17
Version 1.0
*/

@Setter
@Getter
public class ValChangePasswordDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^([a-z0-9\\.]{8,16})$",
            message = "Format Huruf kecil ,numeric dan titik saja min 8 max 25 karakter, contoh : paulch.123")
    private String username;

    @NotNull
    @NotEmpty
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[_#\\-$@])[\\w].{8,15}$",
            message = "Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", @ \"At\", # \"Hash\", atau $ \"Dollar\") setelah 4 kondisi min 9 max 16 alfanumerik, contoh : aB4$12345")
    private String passwordBaru;

    @NotNull
    @NotEmpty
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[_#\\-$@])[\\w].{8,15}$",
            message = "Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", @ \"At\", # \"Hash\", atau $ \"Dollar\") setelah 4 kondisi min 9 max 16 alfanumerik, contoh : aB4$12345")
    private String KonfirmasiPasswordBaru;
}
