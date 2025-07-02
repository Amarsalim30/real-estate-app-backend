package com.amarsalimprojects.real_estate_app.dto.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class PurchaseUnitRequest {

    @NotNull(message = "Buyer ID is required")
    private Long buyerId;

    private Long paymentPlanId;

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;

    @DecimalMin(value = "0.01", message = "Down payment amount must be greater than 0")
    private BigDecimal downPaymentAmount;

    @DecimalMin(value = "0.01", message = "Total amount must be greater than 0")
    private BigDecimal totalAmount;

    @Pattern(regexp = "^254[17]\\d{8}$", message = "Invalid Kenyan phone number format")
    private String mpesaNumber; // Required for MPESA_STKPUSH
}
