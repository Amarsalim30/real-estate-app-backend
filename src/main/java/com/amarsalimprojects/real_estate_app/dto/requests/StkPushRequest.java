package com.amarsalimprojects.real_estate_app.dto.requests;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class StkPushRequest {

    private String mpesaNumber;
    private BigDecimal totalAmount;
    private BigDecimal downPaymentAmount;
    private Long invoiceId;
    private Long unitId;
    private Long buyerId;
}
