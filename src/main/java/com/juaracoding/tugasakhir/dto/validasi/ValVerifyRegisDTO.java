package com.juaracoding.tugasakhir.dto.validasi;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 10/02/2025 14:41
@Last Modified 10/02/2025 14:41
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
public class ValVerifyRegisDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[0-9]{6}$", message = "Masukkan 6 Digit Token Yang Telah Dikirim ke Email")
    private String otp;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com")
    private String email;

}