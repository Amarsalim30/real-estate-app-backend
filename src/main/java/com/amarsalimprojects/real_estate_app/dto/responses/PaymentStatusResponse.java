package com.amarsalimprojects.real_estate_app.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusResponse {

    private Long id;
    private String phoneNumber;
    private BigDecimal amount;
    private String status;
    private String mpesaReceiptNumber;
    private String resultDesc;
    private LocalDateTime createdAt;
    private LocalDateTime callbackReceivedAt;
    private boolean isSuccessful;
    private boolean isPending;
    private String accountReference;
    private String transactionDesc;
}
