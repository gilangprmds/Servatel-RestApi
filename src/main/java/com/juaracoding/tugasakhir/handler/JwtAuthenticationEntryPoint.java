package com.juaracoding.tugasakhir.handler;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 09/02/2025 13:02
@Last Modified 09/02/2025 13:02
Version 1.0
*/
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Component("customAuthenticationEntryPoint")
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Autowired
    ObjectMapper mapper ;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
            throws IOException, ServletException {

        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        Map<String, Object> data = new HashMap<>();
        data.put("status", false);
        data.put("timestamp", Calendar.getInstance().getTime());
        data.put("error", e.getMessage());
//		data.put("error", "Lakukan Otentikasi Terlebih Dahulu !!");
        response.getOutputStream().println(mapper.writeValueAsString(data));
    }
}