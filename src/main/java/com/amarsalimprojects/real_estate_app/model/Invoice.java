package com.amarsalimprojects.real_estate_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.amarsalimprojects.real_estate_app.model.enums.InvoiceStatus;

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

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // FK to Unit (1:1)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false, unique = true)
    private Unit unit;

    // FK to BuyerProfile
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private BuyerProfile buyer;

    // 1:* relationship with Payments
    @Builder.Default
    @OneToMany(mappedBy = "invoice")
    private List<Payment> payments = new ArrayList<>();

    // 1:* relationship with PaymentDetails
    @Builder.Default
    @OneToMany(mappedBy = "invoice")
    private List<PaymentDetail> paymentDetails = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }
    // In Invoice model or service

    public BigDecimal getRemainingAmount(Invoice invoice) {
        BigDecimal totalPaid = invoice.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return invoice.getTotalAmount().subtract(totalPaid);
    }

    public boolean isFullyPaid(Invoice invoice) {
        return getRemainingAmount(invoice).compareTo(BigDecimal.ZERO) <= 0;
    }

    public boolean isOverdue(Invoice invoice) {
        // Add due date logic if you extend the model
        return invoice.getStatus() == InvoiceStatus.OVERDUE;
    }

}
