//package com.juaracoding.tugasakhir.config;
//
//import com.midtrans.Config;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class MidtransConfig {
//
//        @Bean(name = "customMidtransConfig")
//        public Config midtransConfig() {
//            Config config = Config.builder()
//                    .setServerKey("SB-Mid-server-lNuXHFIfHeBizgYcY41-lPL_") // Ganti dengan server key Sandbox kamu
//                    .setClientKey("SB-Mid-client-NoPw1aIkOsI_LrGJ") // Ganti dengan client key Sandbox kamu
//                    .setIsProduction(false) // False karena masih pakai Sandbox
//                    .build();
//            return config;
//        }
//}
