package com.amarsalimprojects.real_estate_app.dto;

package com.amarsalimprojects.real_estate_app.dto;

import com.amarsalimprojects.real_estate_app.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentSummaryDTO {

    private Long invoiceId;
    private BigDecimal invoiceAmount;
    private BigDecimal totalPaid;
    private BigDecimal remainingAmount;
    private Integer paymentCount;
    private List<Payment> payments;
    private Boolean isFullyPaid;

    public Boolean getIsFullyPaid() {
        return remainingAmount != null && remainingAmount.compareTo(BigDecimal.ZERO) <= 0;
    }
}
