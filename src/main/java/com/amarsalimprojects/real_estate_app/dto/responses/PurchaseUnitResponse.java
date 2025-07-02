package com.amarsalimprojects.real_estate_app.dto.responses;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PurchaseUnitResponse {

    private Long invoiceId;
    private Long unitId;
    private Long buyerId;
    private String unitName;
    private String projectName;
    private BigDecimal amount;
    private String paymentMethod;
    private String invoiceStatus; // PENDING, PAID
    private String paymentStatus; // PENDING, COMPLETED, FAILED
    private String message;       // e.g., "STK Push sent to 2547XXXXXX"
}
