package com.amarsalimprojects.real_estate_app.dto.requests;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PurchaseUnitRequest {

    private Long buyerId;
    private Long paymentPlanId;
    private String paymentMethod; // "mpesa_push", "paybill", "wire_transfer"
    private BigDecimal downPaymentAmount;
    private BigDecimal totalAmount;
    private String mpesaNumber; // Optional
    // private BankDetailsDto bankDetails; // Optional
}
