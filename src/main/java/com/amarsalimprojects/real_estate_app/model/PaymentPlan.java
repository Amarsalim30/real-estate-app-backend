package com.amarsalimprojects.real_estate_app.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "payment_plan")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., '24 Month Plan'

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "period_months")
    private Integer periodMonths; // nullable for flexible plans

    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate; // e.g., 0.0500 for 5%

    @Column(name = "min_down_payment_percentage", nullable = false, precision = 5, scale = 4)
    private BigDecimal minDownPaymentPercentage; // e.g., 0.10 = 10%

    @Column(name = "is_flexible", nullable = false)
    private boolean isFlexible = false;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @Column(name = "plan_type", nullable = false)
    private String planType; // cash, installment, flexible

    @ElementCollection
    @CollectionTable(name = "payment_plan_benefits", joinColumns = @JoinColumn(name = "payment_plan_id"))
    @Column(name = "benefit")
    @Builder.Default
    private List<String> benefits = new ArrayList<>();

    @Column(name = "terms_and_conditions", columnDefinition = "TEXT")
    private String termsAndConditions;

    @Column(name = "processing_fee_percentage", precision = 5, scale = 4)
    private BigDecimal processingFeePercentage = BigDecimal.ZERO;

    @Column(name = "early_payment_discount", precision = 5, scale = 4)
    private BigDecimal earlyPaymentDiscount = BigDecimal.ZERO;

    @OneToMany(mappedBy = "paymentPlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<Invoice> invoices = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
