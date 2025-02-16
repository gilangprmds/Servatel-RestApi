package com.juaracoding.tugasakhir.security;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 05/02/2025 20:10
@Last Modified 05/02/2025 20:10
Version 1.0
*/
import org.bouncycastle.crypto.BufferedBlockCipher;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.modes.CBCBlockCipher;
import org.bouncycastle.crypto.paddings.PaddedBufferedBlockCipher;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Hex;

public class Crypto {


    private static final String defaultKey = "349bcade4ca4f10ac0a908a8d2c764a3ee53095f69fbc7e29c95f8f9813fc6dc";

    /** 7e52334e09314807e67ed3882359adf0 --> e07169ee08959f7ba1118af6e607fcb3
     * U2FsdGVkX1/CwKWoBplvmTWuOfWkYAp+ICTG71sKZO0=
     * */
//    private static final String defaultKey = "7e52334e09314807e67ed3882359adf0";
    public static String performEncrypt(String keyText, String plainText) {
        try{
            byte[] key = Hex.decode(keyText.getBytes());
            byte[] ptBytes = plainText.getBytes();
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESLightEngine()));
            cipher.init(true, new KeyParameter(key));
            byte[] rv = new byte[cipher.getOutputSize(ptBytes.length)];
            int oLen = cipher.processBytes(ptBytes, 0, ptBytes.length, rv, 0);
            cipher.doFinal(rv, oLen);
            return new String(Hex.encode(rv));
        } catch(Exception e) {
            return "Error";
        }
    }

    public static String performEncrypt(String cryptoText) {
        return performEncrypt(defaultKey, cryptoText);
    }

    public static String performDecrypt(String keyText, String cryptoText) {
        try {
            byte[] key = Hex.decode(keyText.getBytes());
            byte[] cipherText = Hex.decode(cryptoText.getBytes());
            BufferedBlockCipher cipher = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESLightEngine()));
            cipher.init(false, new KeyParameter(key));
            byte[] rv = new byte[cipher.getOutputSize(cipherText.length)];
            int oLen = cipher.processBytes(cipherText, 0, cipherText.length, rv, 0);
            cipher.doFinal(rv, oLen);
            return new String(rv).trim();
        } catch(Exception e) {
            return "Error";
        }
    }

    public static String performDecrypt(String cryptoText) {
        return performDecrypt(defaultKey, cryptoText);
    }

    public static void main(String[] args) {
        String strToEncrypt = "Andika.123Paul@1234";//put text to encrypt in here
        System.out.println("Encryption Result : "+performEncrypt(strToEncrypt));

        String strToDecrypt = "c02ba3d5895193b023388229063bbd91";//put text to decrypt in here
        String strToDecryptUsername = "cdf02c205039febae3db772a7fa047121cf97b93d879387115ec2a496e3d7582";//put text to decrypt in here
        String strToDecryptPassword = "c88099fb4a86cdebf0af0b524ee58c745a9600406dfe6db6b9e6cb0ffe87b2e6";//put text to decrypt in here
//        String decriptionResult = new Crypto().performDecrypt(strToDecrypt);
        System.out.println("Decryption Result : "+performDecrypt(strToDecrypt));
        System.out.println("Decryption Result username : "+performDecrypt(strToDecryptUsername));
        System.out.println("Decryption Result password : "+performDecrypt(strToDecryptPassword));
        System.out.println("encrypt Result : "+performEncrypt(strToEncrypt));
        System.out.println("encypt Result  email: "+performEncrypt("avivawulantari69@gmail.com"));
        System.out.println("encypt Result pwd : "+performEncrypt("txzwaflkjhexvfie"));
        System.out.println("Decryption Result email : "+performDecrypt("1625283d43347b72f12aa1539299ad2a30d8b10a46cea24d9e63a12f000f2851"));
        System.out.println("Decryption Result pwd : "+performDecrypt("13d9e250a7e868d43915acf2d920b5b36bc2b57dd8b0d45d5d6e4392f2b0fdde"));
    }
}