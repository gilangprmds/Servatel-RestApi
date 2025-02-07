package com.juaracoding.tugasakhir.handler;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 06/02/2025 22:11
@Last Modified 06/02/2025 22:11
Version 1.0
*/
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

/*
IntelliJ IDEA 2024.1.4 (Ultimate Edition)
Build #IU-241.18034.62, built on June 21, 2024
@Author pollc a.k.a. Paul Christian
Java Developer
Created on Tue 20:53
@Last Modified Tue 20:53
Version 1.0
*/
public class ApiError {

    @JsonProperty("server_response")
    private HttpStatus serverResponse;
    private int status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String path;
    @JsonProperty("sub_errors")
    private List<ApiValidationError> subErrors;
    @JsonProperty("error_code")
    private String errorCode;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    ApiError(HttpStatus serverResponse) {
        this.serverResponse = serverResponse;
    }

    ApiError(HttpStatus serverResponse, Throwable ex) {
        this.serverResponse = serverResponse;
        this.message = HttpStatus.INTERNAL_SERVER_ERROR.name();
    }

    ApiError(HttpStatus serverResponse, String message,
             Throwable ex, String path, String errorCode) {
        this.serverResponse = serverResponse;
        this.message = message;
        this.path = path;
        this.errorCode=errorCode;
    }

    public int getStatus() {
        return serverResponse.value();
    }

    public HttpStatus getServerResponse() {
        return serverResponse;
    }

    public void setServerResponse(HttpStatus serverResponse) {
        this.serverResponse = serverResponse;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<ApiValidationError> getSubErrors() {
        return subErrors;
    }

    public void setSubErrors(List<ApiValidationError> subErrors) {
        this.subErrors = subErrors;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}