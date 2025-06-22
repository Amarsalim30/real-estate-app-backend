package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.amarsalimprojects.real_estate_app.enums.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailAuditDTO {

    private Long paymentDetailId;
    private String action;
    private LocalDateTime timestamp;
    private PaymentStatus status;
    private BigDecimal amount;
    private String notes;
}
