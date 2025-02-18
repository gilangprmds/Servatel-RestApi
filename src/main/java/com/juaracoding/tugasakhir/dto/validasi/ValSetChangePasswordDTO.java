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
Created on 18/02/2025 9:03
@Last Modified 18/02/2025 9:03
Version 1.0
*/
@Setter
@Getter
public class ValSetChangePasswordDTO {


    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}$", message = "Masukkan 6 Digit Token Yang Telah Dikirim ke Email")
    private String otp;

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
