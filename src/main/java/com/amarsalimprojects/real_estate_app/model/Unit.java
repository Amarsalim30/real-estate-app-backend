package com.amarsalimprojects.real_estate_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.amarsalimprojects.real_estate_app.enums.ConstructionStage;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitType;

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
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isFeatured;

    private String unitNumber;
    private Integer floor;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer sqft;
    private String description;

    @ElementCollection
    private Set<String> features;
    @ElementCollection
    private List<String> images;

    @Enumerated(EnumType.STRING)
    private UnitStatus status;

    @Enumerated(EnumType.STRING)
    private UnitType type;

    private BigDecimal price;

    //Construction Progress
    @Enumerated(EnumType.STRING)
    private ConstructionStage currentStage;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // FK to Project (belongs to)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // FK to BuyerProfile (1:* from BuyerProfile perspective)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id")
    private BuyerProfile buyer;

    // 1:1 relationship with Invoice
    @OneToOne(mappedBy = "unit")
    private Invoice invoice;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    public void setIsFeatured(boolean featured) {
        this.isFeatured = featured;
    }
}
