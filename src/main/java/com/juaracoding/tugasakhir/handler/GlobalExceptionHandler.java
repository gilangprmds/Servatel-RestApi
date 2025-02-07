package com.juaracoding.tugasakhir.handler;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 06/02/2025 22:20
@Last Modified 06/02/2025 22:20
Version 1.0
*/
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * KODE EXCEPTION
 * VALIDATION       = 01
 * DATA             = 02
 * AUTH             = 03
 * MEDIA / FILE     = 04
 * EXTERNAL API     = 05
 * OTHER            = 99
 */
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private List<ApiValidationError> lsSubError = new ArrayList<>();

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               HttpHeaders headers,
                                                               HttpStatusCode status,
                                                               WebRequest request) {
        lsSubError.clear();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            lsSubError.add(new ApiValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage(),
                    fieldError.getRejectedValue()
            ));
        }
        ApiError apiError =
                new ApiError(HttpStatus.BAD_REQUEST,
                        "Data Tidak Valid !!",
                        ex,request.getDescription(false),"X-01-003");
        apiError.setSubErrors(lsSubError);
        return new ResponseEntity<Object>(apiError,HttpStatus.BAD_REQUEST);
    }

}
