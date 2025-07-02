package com.amarsalimprojects.real_estate_app.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.amarsalimprojects.real_estate_app.config.MpesaConfig;
import com.amarsalimprojects.real_estate_app.dto.responses.StkPushResponse;
import com.amarsalimprojects.real_estate_app.model.Invoice;
import com.amarsalimprojects.real_estate_app.model.MpesaPayment;
import com.amarsalimprojects.real_estate_app.repository.InvoiceRepository;
import com.amarsalimprojects.real_estate_app.repository.MpesaPaymentRepository;

@Service
public class MpesaStkService {

    private final MpesaConfig config;
    private final MpesaAuthService authService;
    private final RestTemplate restTemplate;
    private final MpesaPaymentRepository mpesaPaymentRepository;
    private final InvoiceRepository invoiceRepository;

    BigDecimal testAmount = new BigDecimal("1.00");

    public MpesaStkService(MpesaConfig config, MpesaAuthService authService,
            RestTemplate restTemplate, MpesaPaymentRepository mpesaPaymentRepository,
            InvoiceRepository invoiceRepository) {
        this.config = config;
        this.authService = authService;
        this.restTemplate = restTemplate;
        this.mpesaPaymentRepository = mpesaPaymentRepository;
        this.invoiceRepository = invoiceRepository;

    }

    public StkPushResponse initiateStkPush(String phone, BigDecimal amount, Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String password = Base64.getEncoder().encodeToString(
                (config.getShortcode() + config.getPasskey() + timestamp).getBytes()
        );
        Map<String, Object> body = new HashMap<>();
        body.put("BusinessShortCode", config.getShortcode());
        body.put("Password", password);
        body.put("Timestamp", timestamp);
        body.put("TransactionType", "CustomerPayBillOnline");
        body.put("Amount", testAmount.intValue());
        body.put("PartyA", phone);
        body.put("PartyB", config.getShortcode());
        body.put("PhoneNumber", phone);
        body.put("CallBackURL", config.getCallbackUrl());
        body.put("AccountReference", "AMAR");//+ invoiceId
        body.put("TransactionDesc", "INV payment");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authService.getAccessToken());

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                config.getBaseUrl() + config.getStkPushUrl(), request, Map.class
        );

        if (response.getBody() == null || response.getBody().get("ResponseCode") == null) {
            return StkPushResponse.failure("M-Pesa STK Push failed or returned no data");
        }

        Map<String, Object> responseBody = response.getBody();

        MpesaPayment payment = MpesaPayment.builder()
                .invoice(invoice)
                .amount(amount)
                .status("PENDING")
                .checkoutRequestId((String) responseBody.get("CheckoutRequestID"))
                .merchantRequestId((String) responseBody.get("MerchantRequestID"))
                .phoneNumber(phone)
                .build();

        mpesaPaymentRepository.save(payment);

        return StkPushResponse.success(
                payment.getMerchantRequestId(),
                payment.getCheckoutRequestId(),
                (String) responseBody.get("ResponseCode"),
                (String) responseBody.get("ResponseDescription"),
                (String) responseBody.get("CustomerMessage")
        );

    }
}
