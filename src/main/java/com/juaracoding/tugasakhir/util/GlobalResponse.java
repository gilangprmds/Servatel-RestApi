package com.juaracoding.tugasakhir.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juaracoding.tugasakhir.handler.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 06/02/2025 14:33
@Last Modified 06/02/2025 14:33
Version 1.0
*/
public class GlobalResponse {

    public static ResponseEntity<Object> dataTidakValid(
            String errorCode,
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA TIDAK VALID",
                HttpStatus.BAD_REQUEST,
                null,errorCode, request);
    }

    public static ResponseEntity<Object> dataGagalDisimpan(
            String errorCode,
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA GAGAL DISIMPAN",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,errorCode,request);
    }

    public static ResponseEntity<Object> dataGagalDiubah(
            String errorCode,
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA GAGAL DIUBAH",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,errorCode,request);
    }

    public static ResponseEntity<Object> dataGagalDihapus(
            String errorCode,
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA GAGAL DIHAPUS",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,errorCode,request);
    }

    public static ResponseEntity<Object> dataBerhasilDisimpan(
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA BERHASIL DISIMPAN",
                HttpStatus.CREATED,
                null,null,request);
    }

    public static ResponseEntity<Object> dataBerhasilDiubah(
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA BERHASIL DIUBAH",
                HttpStatus.OK,
                null,null,request);
    }

    public static ResponseEntity<Object> dataBerhasilDihapus(
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA BERHASIL DIHAPUS",
                HttpStatus.OK,
                null,null,request);
    }

    public static ResponseEntity<Object> dataTidakDitemukan(
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA TIDAK DITEMUKAN",
                HttpStatus.BAD_REQUEST,
                null,"X01001",request); //X disini berarti global
    }

    public static ResponseEntity<Object> dataResponseList(
            Map<String,Object> map ,
            HttpServletRequest request

    ){
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.OK,
                map,null,request);
    }

    public static ResponseEntity<Object> dataResponseObject(
            Object object,
            HttpServletRequest request

    ){
        return new ResponseHandler().handleResponse("OK",
                HttpStatus.OK,
                object,null,request);
    }

    public static ResponseEntity<Object> dataGagalDiakses(
            String errorCode,
            HttpServletRequest request
    ){
        return new ResponseHandler().handleResponse("DATA GAGAL DIAKSES",
                HttpStatus.INTERNAL_SERVER_ERROR,
                null,errorCode,request);
    }

    public static ResponseEntity<Object> formatHarusExcel(
            HttpServletRequest request
    ){
        return  new ResponseHandler().handleResponse("Format Harus Excel",
                HttpStatus.BAD_REQUEST,
                null,"X04001",request);
    }

    public static ResponseEntity<Object> dataFileKosong(
            HttpServletRequest request
    ){
        return  new ResponseHandler().handleResponse("Tidak Ada Data Di File Excel",
                HttpStatus.BAD_REQUEST,
                null,"X04002",request);
    }

    public static ResponseEntity<Object> fileExcelGagalDiproses(
            String errorCode,
            HttpServletRequest request
    ){
        return  new ResponseHandler().handleResponse("UPLOAD EXCEL GAGAL",
                HttpStatus.BAD_REQUEST,
                null,"X04002",request);
    }

    /**
     * Digunakan untuk functional download file dll
     * yang bersifat tidak mengembalikan data response
     */
    public static void manualResponse(HttpServletResponse response, ResponseEntity<Object> respObject){
        try {
            response.getWriter().write(convertObjectToJson(respObject.getBody()));
            response.setStatus(respObject.getStatusCodeValue());
        } catch (IOException e) {

        }
    }

    public static String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
