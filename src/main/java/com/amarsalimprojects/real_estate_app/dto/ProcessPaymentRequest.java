package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;

import com.amarsalimprojects.real_estate_app.enums.PaymentMethod;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest {

    private Long invoiceId;
    private Long buyerId;
    private BigDecimal amount;
    private PaymentMethod method;
    private String referenceNumber;
    private String notes;
}
