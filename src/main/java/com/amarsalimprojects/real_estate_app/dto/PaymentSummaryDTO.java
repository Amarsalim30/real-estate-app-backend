package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;
import java.util.List;

import com.amarsalimprojects.real_estate_app.model.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSummaryDTO {

    private Long invoiceId;
    private BigDecimal totalInvoiceAmount;
    private BigDecimal totalPaidAmount;
    private BigDecimal remainingAmount;
    private Long numberOfPayments;
    private boolean isFullyPaid;
    private List<Payment> payments;
}
