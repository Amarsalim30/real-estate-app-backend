package com.amarsalimprojects.real_estate_app.dto.requests;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCheckPaymentRequest {

    private Long paymentId;
    private Long invoiceId;
    private Long buyerId;
    private BigDecimal amount;
    private String transactionId;
    private String checkNumber;
    private String bankName;
    private String accountNumber;
}
