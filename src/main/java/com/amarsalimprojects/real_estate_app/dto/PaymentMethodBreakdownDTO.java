package com.amarsalimprojects.real_estate_app.dto;

import com.amarsalimprojects.real_estate_app.model.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentMethodBreakdownDTO {

    private PaymentMethod paymentMethod;
    private Long count;
    private BigDecimal totalAmount;
    private Double percentage;
}
