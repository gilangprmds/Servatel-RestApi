package com.juaracoding.tugasakhir.util;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 21:09
@Last Modified 07/02/2025 21:09
Version 1.0
*/
import com.juaracoding.tugasakhir.security.Crypto;
import com.juaracoding.tugasakhir.security.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class GlobalFunction {

    public static Map<String,Object> convertClassToObject(Object object){
        Map<String, Object> map = new LinkedHashMap<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field: fields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), field.get(object));
            } catch (IllegalAccessException e) {
//                LoggingFile.exceptionStringz("GlobalFunction","convertClassToObject",e, OtherConfig.getFlagLogging());
            }
        }
        return map;
    }



    public static String camelToStandar(String str)
    {
        StringBuilder sb = new StringBuilder();
        char c = str.charAt(0);
        sb.append(Character.toLowerCase(c));
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (Character.isUpperCase(ch)) {
                sb.append(' ').append(Character.toLowerCase(ch));
            }
            else {
                sb.append(ch);
            }
        }

        return sb.toString();
    }

    public static Map<String,Object> extractToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        token = token.substring(7);
        token = Crypto.performDecrypt(token);
        return new JwtUtility().mappingBodyToken(token , new HashMap<>());
    }
}

