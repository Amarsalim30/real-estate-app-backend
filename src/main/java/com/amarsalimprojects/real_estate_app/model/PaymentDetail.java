package com.amarsalimprojects.real_estate_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.amarsalimprojects.real_estate_app.enums.PaymentMethod;
import com.amarsalimprojects.real_estate_app.enums.PaymentStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)

public class PaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod method;

    private String transactionId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    @JsonBackReference(value = "buyer-payment-details")
    private BuyerProfile buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    @JsonBackReference(value = "invoice-payment-details")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    @JsonBackReference(value = "payment-payment-details")
    private Payment payment;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "bankName", column = @Column(name = "bank_details_bank_name")),
        @AttributeOverride(name = "accountLast4", column = @Column(name = "bank_details_account_last4")),
        @AttributeOverride(name = "routingNumber", column = @Column(name = "bank_details_routing_number"))
    })
    private BankDetails bankDetails;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "lastFour", column = @Column(name = "card_details_last_four")),
        @AttributeOverride(name = "brand", column = @Column(name = "card_details_brand")),
        @AttributeOverride(name = "expiryMonth", column = @Column(name = "card_details_expiry_month")),
        @AttributeOverride(name = "expiryYear", column = @Column(name = "card_details_expiry_year"))
    })
    private CardDetails cardDetails;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "checkNumber", column = @Column(name = "check_details_check_number")),
        @AttributeOverride(name = "bankName", column = @Column(name = "check_details_bank_name")),
        @AttributeOverride(name = "accountNumber", column = @Column(name = "check_details_account_number"))
    })
    private CheckDetails checkDetails;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
class BankDetails {

    private String bankName;
    private String accountLast4;
    private String routingNumber;
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
class CardDetails {

    private String lastFour;
    private String brand;
    private String expiryMonth;
    private String expiryYear;
}

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
class CheckDetails {

    private String checkNumber;
    private String bankName;
    private String accountNumber;
}
