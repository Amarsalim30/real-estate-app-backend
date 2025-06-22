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
public class PaymentStatisticsDTO {

    private Long totalPayments;
    private Long completedPayments;
    private Long pendingPayments;
    private Long failedPayments;
    private BigDecimal totalAmountCollected;
    private BigDecimal pendingAmount;
    private Double completionRate;

    public Double getCompletionRate() {
        if (totalPayments == 0) {
            return 0.0;
        }
        return (completedPayments.doubleValue() / totalPayments.doubleValue()) * 100;
    }
}
