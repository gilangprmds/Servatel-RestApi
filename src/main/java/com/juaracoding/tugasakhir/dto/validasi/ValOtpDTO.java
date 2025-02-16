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
Created on 14/02/2025 19:08
@Last Modified 14/02/2025 19:08
Version 1.0
*/
@Setter
@Getter
public class ValOtpDTO {


    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}$", message = "Masukkan 6 Digit Token Yang Telah Dikirim ke Email")
    private String otp;
}
