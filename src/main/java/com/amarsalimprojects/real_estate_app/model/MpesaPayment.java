package com.amarsalimprojects.real_estate_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "mpesa_payments", indexes = {
    @Index(name = "idx_mpesa_checkout_request", columnList = "checkout_request_id", unique = true),
    @Index(name = "idx_mpesa_merchant_request", columnList = "merchant_request_id"),
    @Index(name = "idx_mpesa_phone", columnList = "phone_number"),
    @Index(name = "idx_mpesa_status", columnList = "status"),
    @Index(name = "idx_mpesa_receipt", columnList = "mpesa_receipt_number"),
    @Index(name = "idx_mpesa_buyer", columnList = "buyer_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MpesaPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "phone_number", nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "checkout_request_id", unique = true, length = 100)
    private String checkoutRequestId;

    @Column(name = "merchant_request_id", length = 100)
    private String merchantRequestId;

    @Column(name = "mpesa_receipt_number", length = 50)
    private String mpesaReceiptNumber;

    @Column(name = "transaction_date", length = 20)
    private String transactionDate;

    @Column(name = "result_code", length = 10)
    private String resultCode;

    @Column(name = "result_desc", columnDefinition = "TEXT")
    private String resultDesc;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "PENDING"; // PENDING, SUCCESS, FAILED, CANCELLED, TIMEOUT

    @Column(name = "account_reference", length = 100)
    private String accountReference;

    @Column(name = "transaction_desc", columnDefinition = "TEXT")
    private String transactionDesc;

    @Column(name = "callback_received_at")
    private LocalDateTime callbackReceivedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    @JsonBackReference(value = "buyer-mpesa-payments")
    private BuyerProfile buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    @JsonBackReference(value = "invoice-mpesa-payments")
    private Invoice invoice;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_detail_id")
    private PaymentDetail paymentDetail;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business methods
    public boolean isSuccessful() {
        return "SUCCESS".equals(status) && "0".equals(resultCode);
    }

    public boolean isPending() {
        return "PENDING".equals(status);
    }

    public boolean isFailed() {
        return "FAILED".equals(status)
                || ("SUCCESS".equals(status) && !"0".equals(resultCode));
    }
}
