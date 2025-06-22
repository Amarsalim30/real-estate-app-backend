package com.amarsalimprojects.real_estate_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.amarsalimprojects.real_estate_app.model.UserManagement.Buyer;
import com.amarsalimprojects.real_estate_app.model.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.enums.UnitType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
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
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    private String unitNumber;
    private Integer floor;
    @Enumerated(EnumType.STRING)
    private UnitType type;

    private Integer bedrooms;
    private Integer bathrooms;
    private Integer sqft;
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    private UnitStatus status;
    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    private List<String> features;

    @ElementCollection
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reserved_by")
    private Buyer reservedBy;
    private LocalDate reservedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sold_to")
    private Buyer soldTo;
    private LocalDate soldDate;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "unit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

}
