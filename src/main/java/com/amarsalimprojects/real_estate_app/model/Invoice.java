package com.amarsalimprojects.real_estate_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.amarsalimprojects.real_estate_app.enums.InvoiceStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    private BigDecimal totalAmount;
    private LocalDate issuedDate;
    private LocalDate dueDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // FK to Unit (1:1)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false, unique = true)
    @JsonIgnore
    private Unit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    @JsonBackReference(value = "buyer-invoices")
    private BuyerProfile buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_plan_id", nullable = false)
    @JsonBackReference(value = "payment-plan-invoices")
    private PaymentPlan paymentPlan;

    @Builder.Default
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "invoice-payments")
    private List<Payment> payments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "invoice-payment-details")
    private List<PaymentDetail> paymentDetails = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference(value = "invoice-mpesa-payments")
    private List<MpesaPayment> mpesaPayments = new ArrayList<>();

    @Column(name = "checkout_request_id", length = 100)
    private String checkoutRequestId;

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business Logic Methods (Now using this, not parameterized)
    public BigDecimal getRemainingAmount() {
        BigDecimal totalPaid = this.payments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return this.totalAmount.subtract(totalPaid);
    }

    public boolean isFullyPaid() {
        return getRemainingAmount().compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isOverdue() {
        return this.status == InvoiceStatus.OVERDUE;
    }

    public boolean hasSuccessfulMpesaPayment() {
        return this.mpesaPayments.stream()
                .anyMatch(MpesaPayment::isSuccessful);
    }

    public BigDecimal getTotalMpesaPayments() {
        return this.mpesaPayments.stream()
                .filter(MpesaPayment::isSuccessful)
                .map(MpesaPayment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
