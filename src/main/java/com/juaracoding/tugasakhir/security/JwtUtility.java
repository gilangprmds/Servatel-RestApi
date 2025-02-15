package com.juaracoding.tugasakhir.security;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 07/02/2025 21:14
@Last Modified 07/02/2025 21:14
Version 1.0
*/
import com.juaracoding.tugasakhir.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtility {

    /**
     * Function disini hanya menerima token JWT yang sudah di decrypt
     * Yang dapat di claims disini adalah key yang diinput dari api Login
     */
    public Map<String,Object> mappingBodyToken(String token,
                                               Map<String,Object> mapz){
        /** claims adalah data payload yang ada di token
         * PASTIKAN YANG DIISI SAAT PROSES LOGIN SAMA SAAT PROSES CLAIMS
         */
        Claims claims = getAllClaimsFromToken(token);
        mapz.put("userId",claims.get("uid"));
        mapz.put("username",claims.get("un"));
        mapz.put("email",claims.get("ml"));//untuk email
        mapz.put("firstName",claims.get("fn"));
        mapz.put("lastName",claims.get("ln"));
        mapz.put("noHp",claims.get("pn"));
        return mapz;
    }

    /**
     * KONFIGURASI CUSTOMISASI BERAKHIR DISINI
     * KONFIGURASI UNTUK JWT DIMULAI DARI SINI
     */
//    username dari token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //parameter token habis waktu nya
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    //kita dapat mengambil informasi dari token dengan menggunakan secret key
    //disini juga validasi dari expired token dan lihat signature  dilakukan
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(JwtConfig.getJwtSecret()).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //generate token untuk user --> Untuk di fungsi Login
    public String generateToken(UserDetails userDetails, Map<String,Object> claims) {
        claims = (claims==null)?new HashMap<String,Object>():claims;
        return doGenerateToken(claims, userDetails.getUsername());
    }
    /** proses yang dilakukan saat membuat token adalah :
     mendefinisikan claim token seperti penerbit (Issuer) , waktu expired , subject dan ID
     generate signature dengan menggunakan secret key dan algoritma HS512 (HMAC - SHA),
     */
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        Long timeMilis = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(timeMilis))
                .setExpiration(new Date(timeMilis + Long.parseLong(JwtConfig.getJwtExpiration())))
                .signWith(SignatureAlgorithm.HS512, JwtConfig.getJwtSecret()).compact();
    }

    public Boolean validateToken(String token) {
        /** Sudah otomatis tervalidaasi jika expired date masih aktif */
        String username = getUsernameFromToken(token);
        return (username!=null && !isTokenExpired(token));
    }
    /**
     * KONFIGURASI UNTUK JWT BERAKHIR DI SINI
     */
}
