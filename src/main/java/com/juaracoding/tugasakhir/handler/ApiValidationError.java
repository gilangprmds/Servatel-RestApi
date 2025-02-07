package com.juaracoding.tugasakhir.handler;

import lombok.Getter;
import lombok.Setter;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 06/02/2025 22:16
@Last Modified 06/02/2025 22:16
Version 1.0
*/
@Getter
@Setter
public class ApiValidationError {

    private String field;
    private String message;
    private Object rejectedValue;

    public ApiValidationError(String field, String message, Object rejectedValue) {
        this.field = field;
        this.message = message;
        this.rejectedValue = rejectedValue;
    }
}