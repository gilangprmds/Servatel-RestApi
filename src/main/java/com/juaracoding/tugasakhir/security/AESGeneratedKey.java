package com.juaracoding.tugasakhir.security;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 05/02/2025 20:07
@Last Modified 05/02/2025 20:07
Version 1.0
*/
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class AESGeneratedKey {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Tambahkan Bouncy Castle Provider
        Security.addProvider(new BouncyCastleProvider());
        try {
            // Inisialisasi generator kunci AES dengan Bouncy Castle
            KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");

            // Atur panjang kunci (misalnya: 128, 192, atau 256 bit)
            keyGen.init(256);
            // Tampilkan kunci AES

            // Generate kunci AES
            SecretKey aesKey = keyGen.generateKey();
//            System.out.println(aesKey);
            // Tampilkan kunci AES
            System.out.println("AES Key: " + bytesToHex(aesKey.getEncoded()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getKey() {
        SecretKey aesKey = null;
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES", "BC");
            // Atur panjang kunci (misalnya: 128, 192, atau 256 bit)
            keyGen.init(128);
            // Generate kunci AES
            aesKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bytesToHex(aesKey.getEncoded());
    }

    // Helper method untuk mengubah byte array menjadi string heksadesimal
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}