// package com.amarsalimprojects.real_estate_app.dto.responses;

// import java.math.BigDecimal;
// import java.time.LocalDate;
// import java.time.LocalDateTime;

// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Data;
// import lombok.NoArgsConstructor;

// @Data
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class InvoiceResponse {
//     private Long id;
//     private String invoiceNumber;

//     private String status; // Enum as string
//     private BigDecimal totalAmount;
//     private BigDecimal remainingAmount;

//     private LocalDate issuedDate;
//     private LocalDate dueDate;

//     private LocalDateTime createdAt;
//     private LocalDateTime updatedAt;

//     // Optional related info
//     private Long buyerId;
//     private String buyerName;

//     private Long unitId;
//     private String unitNumber;

//     private Long paymentPlanId;
//     private String paymentPlanName;

//     private boolean hasSuccessfulMpesaPayment;
// }
