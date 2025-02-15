package com.juaracoding.tugasakhir.security;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 05/02/2025 20:22
@Last Modified 05/02/2025 20:22
Version 1.0
*/
import java.util.function.Function;

public class BcryptImpl {

    private static final BcryptCustom bcrypt = new BcryptCustom(11);

    public static String hash(String password) {
        return bcrypt.hash(password);
    }

    public static boolean verifyAndUpdateHash(String password,
                                              String hash,
                                              Function<String, Boolean> updateFunc) {
        return bcrypt.verifyAndUpdateHash(password, hash, updateFunc);
    }

    public static boolean verifyHash(String password , String hash)
    {
        return bcrypt.verifyHash(password,hash);
    }

    public static void main(String[] args) {

        String strUserName = "andika.1234Apalah@1234 ";
        System.out.println(hash(strUserName));
        System.out.println(hash(strUserName).length());

        System.out.println(verifyHash("906442","$2a$11$VS3bybvoDaSVYDFfhPCiqeOI4zh6kmsIXhlzVwrdMbQ3/qnM32Iay"));
        System.out.println(verifyHash(strUserName,"$2a$11$r9Uj5UuqePNLoYRmj1yUguDie6cZ0Co/3YudQFVvyPqQE7VwIkzJ6"));
        System.out.println(verifyHash(strUserName,"$2a$11$YUVY8jlm9v6lrg7Ahr9lEewpKDl8Zj1BIZ6/hXA0mtZG4hcRdy8pm"));
    }
}