package com.amarsalimprojects.real_estate_app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "mpesa")
@Validated
@Getter
@Setter
public class MpesaConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @NotBlank(message = "M-Pesa consumer key is required")
    private String consumerKey;

    @NotBlank(message = "M-Pesa consumer secret is required")
    private String consumerSecret;

    @NotBlank(message = "M-Pesa passkey is required")
    private String passkey;

    @NotBlank(message = "M-Pesa shortcode is required")
    private String shortcode;

    @NotBlank(message = "M-Pesa base URL is required")
    private String baseUrl = "https://sandbox.safaricom.co.ke";

    private String stkPushUrl = "/mpesa/stkpush/v1/processrequest";

    @NotBlank(message = "M-Pesa callback URL is required")
    private String callbackUrl = "https://b306-129-222-187-138.ngrok-free.app/api/payments/mpesa/callback";

    private String tokenUrl = "/oauth/v1/generate?grant_type=client_credentials";

    private long phoneNumberSender = 254708374149L;
    // Timeout configurations
    private int connectionTimeout = 30000; // 30 seconds
    private int readTimeout = 60000; // 60 seconds

    // Retry configurations
    private int maxRetries = 3;
    private long retryDelay = 2000; // 2 seconds

    // Security
    private boolean enableSignatureVerification = true;

    // Convenience methods to get full URLs
    public String getFullTokenUrl() {
        return baseUrl + tokenUrl;
    }

    public String getFullStkPushUrl() {
        return baseUrl + stkPushUrl;
    }
}
