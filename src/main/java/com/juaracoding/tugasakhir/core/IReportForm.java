package com.juaracoding.tugasakhir.core;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 11/02/2025 14:26
@Last Modified 11/02/2025 14:26
Version 1.0
*/
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface IReportForm<G> {

    public ResponseEntity<Object> uploadDataExcel(MultipartFile multipartFile, HttpServletRequest request);//061-070
    public List<G> convertListWorkBookToListEntity(List<Map<String, String>> workBookData, Long userId);//071-080
    public void downloadReportExcel(String column, String value, HttpServletRequest request, HttpServletResponse response);//081-090
    public void generateToPDF(String column, String value, HttpServletRequest request, HttpServletResponse response);//091-100
}