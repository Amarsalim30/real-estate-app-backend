package com.amarsalimprojects.real_estate_app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.amarsalimprojects.real_estate_app.model.enums.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
    private String address;
    private String city;
    private String state;
    private String zipCode;

    private Integer totalUnits;
    private Integer availableUnits;
    private Integer reservedUnits;
    private Integer soldUnits;

    private Integer constructionProgress;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate expectedCompletion;
    private String developer;

    @ElementCollection
    private List<String> images;
    @ElementCollection
    private List<String> amenities;
    @Embedded
    private PriceRange priceRange;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Unit> units = new ArrayList<>();

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Invoice> invoices;

    // Add Units to Project and set Project to Unit
    public void addUnit(Unit unit) {
        if (!this.units.contains(unit)) {
            unit.setProject(this);
            this.units.add(unit);
        }
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
        unit.setProject(null);
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();
    }

}

// public enum ProjectType {
//     HOUSE,
//     APARTMENT,
//     CONDO,
//     TOWNHOUSE,
//     COMMERCIAL,
//     LAND
// }
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
class PriceRange {

    private BigDecimal min;
    private BigDecimal max;
}
