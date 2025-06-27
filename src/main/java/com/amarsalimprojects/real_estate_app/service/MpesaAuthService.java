package com.amarsalimprojects.real_estate_app.service;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amarsalimprojects.real_estate_app.config.MpesaConfig;

@Service
public class MpesaAuthService {

    private final MpesaConfig config;
    private final RestTemplate restTemplate;

    private String token;
    private Instant expiry;

    public MpesaAuthService(MpesaConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    public synchronized String getAccessToken() {
        if (token != null && Instant.now().isBefore(expiry)) {
            return token;
        }

        String creds = Base64.getEncoder().encodeToString(
                (config.getConsumerKey() + ":" + config.getConsumerSecret()).getBytes()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + creds);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                config.getBaseUrl() + config.getTokenUrl(), HttpMethod.GET, request, Map.class
        );

        if (response.getStatusCode().is2xxSuccessful()) {
            Map body = response.getBody();
            token = (String) body.get("access_token");
            Integer expiresIn = (Integer) body.get("expires_in");
            expiry = Instant.now().plusSeconds(expiresIn - 60);
            return token;
        }
        throw new RuntimeException("Failed to get access token");
    }
}
