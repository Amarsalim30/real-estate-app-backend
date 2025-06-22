package com.amarsalimprojects.real_estate_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailStatisticsDTO {

    private Long totalPaymentDetails;
    private Long completedPaymentDetails;
    private Long pendingPaymentDetails;
    private Long failedPaymentDetails;
    private BigDecimal totalAmountProcessed;
    private BigDecimal pendingAmount;
    private Double completionRate;

    public Double getCompletionRate() {
        if (totalPaymentDetails == 0) {
            return 0.0;
        }
        return (completedPaymentDetails.doubleValue() / totalPaymentDetails.doubleValue()) * 100;
    }
}
