package com.juaracoding.tugasakhir.util;

/*
Created By IntelliJ IDEA 2024.3 (Community Edition)
Build #IC-243.21565.193, built on November 13, 2024
@Author USER Febby Tri Andika
Java Developer
Created on 10/02/2025 12:38
@Last Modified 10/02/2025 12:38
Version 1.0
*/
import com.juaracoding.tugasakhir.config.SMTPConfig;
import com.juaracoding.tugasakhir.core.SMTPCore;

import java.util.Random;

public class SendMailOTP {

    public static void verifyRegisOTP(String subject, String firstName ,String lastName,String email,String token) {
        try{
            String[] strVerify = new String[4];
            strVerify[0] = subject;
            strVerify[1] = firstName;
            strVerify[1] = lastName;
            strVerify[2] = token;
            String  strContent = new ReadTextFileSB("ver_regis.html").getContentFile();
            strContent = strContent.replace("#JKVM3NH",strVerify[0]);//Kepentingan
            strContent = strContent.replace("XF#31NN",strVerify[1]);//Nama Lengkap
            strContent = strContent.replace("8U0_1GH$",strVerify[2]);//TOKEN
            final String content = strContent;
            System.out.println(SMTPConfig.getEmailHost());
            String [] strEmail = {email};
            String [] strImage = null;//isi kalau mau menggunakan attachment, parameter nya jg diubah
            SMTPCore sc = new SMTPCore();
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    sc.sendMailWithAttachment(strEmail,
                            subject,
                            content,
                            "SSL",strImage);
                }
            });
            t.start();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}