package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCardPaymentRequest {

    private Long paymentId;
    private Long invoiceId;
    private Long buyerId;
    private BigDecimal amount;
    private String transactionId;
    private String lastFour;
    private String brand;
    private String expiryMonth;
    private String expiryYear;
}
