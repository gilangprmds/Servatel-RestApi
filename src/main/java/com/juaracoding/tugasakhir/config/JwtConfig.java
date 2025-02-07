package com.juaracoding.tugasakhir.config;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 21:25
@Last Modified 07/02/2025 21:25
Version 1.0
*/
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:jwtproperties.properties")
public class JwtConfig {


    private static String jwtSecret;
    private static String jwtExpiration;

    public static String getJwtSecret() {
        return jwtSecret;
    }

    @Value("${jwt.secret}")
    private void setJwtSecret(String jwtSecret) {
        JwtConfig.jwtSecret = jwtSecret;
    }

    public static String getJwtExpiration() {
        return jwtExpiration;
    }

    @Value("${jwt.expiration}")
    private void setJwtExpiration(String jwtExpiration) {
        JwtConfig.jwtExpiration = jwtExpiration;
    }
}