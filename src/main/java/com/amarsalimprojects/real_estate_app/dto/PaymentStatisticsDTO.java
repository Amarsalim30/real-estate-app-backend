package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatisticsDTO {

    private Long totalPayments;
    private BigDecimal totalAmount;
    private Long completedPayments;
    private BigDecimal completedAmount;
    private Long pendingPayments;
    private BigDecimal pendingAmount;
    private Long failedPayments;
    private BigDecimal failedAmount;
    private BigDecimal averagePaymentAmount;
    private Long paymentsToday;
    private BigDecimal amountToday;
    private Long paymentsThisMonth;
    private BigDecimal amountThisMonth;
}
