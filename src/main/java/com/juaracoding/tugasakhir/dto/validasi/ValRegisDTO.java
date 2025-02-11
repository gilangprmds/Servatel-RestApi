package com.juaracoding.tugasakhir.dto.validasi;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 10/02/2025 14:38
@Last Modified 10/02/2025 14:38
Version 1.0
*/
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Setter
@Getter
public class ValRegisDTO {

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^([a-z0-9\\.]{8,16})$",
            message = "Format Huruf kecil ,numeric dan titik saja min 8 max 25 karakter, contoh : paulch.123")
    private String username;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@_#\\-$])[\\w].{8,15}$",
            message = "Format minimal 1 angka, 1 huruf kecil, 1 huruf besar, 1 spesial karakter (_ \"Underscore\", - \"Hyphen\", # \"Hash\", atau $ \"Dollar\" atau @ \"At\") setelah 4 kondisi min 9 max 16 alfanumerik, contoh : aB4$12345")
    private String password;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(?=.{1,256})(?=.{1,64}@.{1,255}$)(?:(?![.])[a-zA-Z0-9._%+-]+(?:(?<!\\\\)[.][a-zA-Z0-9-]+)*?)@[a-zA-Z0-9.-]+(?:\\.[a-zA-Z]{2,50})+$",
            message = "Format tidak valid contoh : user_name123@sub.domain.com")
    private String email;
    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[\\w\\s\\.\\,]{20,255}$",
            message = "Format Alamat Tidak Valid min 20 maks 255, contoh : Jln. Kenari 2B jakbar 11480")
    private String address;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^(62|\\+62|0)8[0-9]{9,13}$",
            message = "Format No HP Tidak Valid , min 9 max 13 setelah angka 8, contoh : (0/62/+62)81111111")
    @JsonProperty("no-hp")
    private String noHp;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonProperty("tanggal-lahir")
    private LocalDate tanggalLahir;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s]{4,25}$", message = "Hanya Alfabet dan spasi Minimal 4 Maksimal 25")
    @JsonProperty("first-name")
    private String firstName;

    @NotNull
    @NotBlank
    @NotEmpty
    @Pattern(regexp = "^[a-zA-Z\\s]{4,25}$", message = "Hanya Alfabet dan spasi Minimal 4 Maksimal 25")
    @JsonProperty("last-name")
    private String lastName;
}