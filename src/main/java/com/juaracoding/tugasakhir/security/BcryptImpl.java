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

        String strUserName = "174857";
        System.out.println(hash(strUserName));
        System.out.println(hash(strUserName).length());

        System.out.println(verifyHash("326586","$2a$11$ZUT6/omxkGTa/GpkaPRgGu7WomsDDb95lWdut/PNQeU1yjofT76hi"));
        System.out.println(verifyHash(strUserName,"$2a$11$pZyVH69dHnMGHKM2op5udO2qEzuBk9zYTy3GTyqko/AeHgHs6pNWS"));
        System.out.println(verifyHash(strUserName,"$2a$11$k9SaQKkbtcEEtgSR3jzOY.4eMfvwZoYwXDNtCkox44ywZlnMk.qT."));
        System.out.println(verifyHash(strUserName,"$2a$11$k9SaQKkbtcEEtgSR3jzOY.4eMfvwZoYwXDNtCkox44ywZlnMk.qT."));
    }
}