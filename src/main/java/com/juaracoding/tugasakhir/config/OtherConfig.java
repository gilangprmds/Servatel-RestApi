package com.juaracoding.tugasakhir.config;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:other.properties")
public class OtherConfig {

    private static String enableLogFile;
    private static String enableAutomation;

    public static String getEnableAutomation() {
        return enableAutomation;
    }

    @Value("${enable.automation}")
    private void setEnableAutomation(String enableAutomation) {
        OtherConfig.enableAutomation = enableAutomation;
    }

    public static String getEnableLogFile() {
        return enableLogFile;
    }

    @Value("${enable.logfile}")
    private void setEnableLogFile(String enableLogFile) {
//        OtherConfig.enableLogFile = Crypto.performDecrypt(enableLogFile);
        OtherConfig.enableLogFile = enableLogFile;
    }

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
