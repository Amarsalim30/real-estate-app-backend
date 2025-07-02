package com.amarsalimprojects.real_estate_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.amarsalimprojects.real_estate_app.config.MpesaConfig;

@EnableConfigurationProperties(MpesaConfig.class)
@SpringBootApplication
public class RealEstateAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealEstateAppApplication.class, args);
    }

}
// Appended text
//
