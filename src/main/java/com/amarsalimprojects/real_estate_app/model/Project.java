package com.amarsalimprojects.real_estate_app.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.amarsalimprojects.real_estate_app.enums.ProjectStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;
    private String address;
    private String county;
    private String subCounty;

    private Double latitude;
    private Double longitude;
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    //construction management
    private String developerName;
    private float constructionProgress;
    private LocalDateTime startDate;
    private LocalDateTime targetCompletionDate;
    private LocalDateTime completionDate;
    // Add this to support admin control of completion
    private boolean adminSignedOff;

    //selling points
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private BigDecimal DownPaymentPercentage;
    @ElementCollection
    private List<String> images;

    @ElementCollection
    private Set<String> amenities;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // 1:* relationship with Units
    @Builder.Default
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Unit> units = new ArrayList<>();

    public void addUnit(Unit unit) {
        units.add(unit);
        unit.setProject(this); // set reverse side
    }

    public void removeUnit(Unit unit) {
        units.remove(unit);
        unit.setProject(null); // break link
    }

    // Returns 100 if adminSignedOff is true
    public float getDisplayableProgress() {
        return adminSignedOff ? 100f : constructionProgress;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = updatedAt = LocalDateTime.now();

        // Set default construction stage info
        if (status == null) {
            status = ProjectStatus.UNDER_CONSTRUCTION; // default project status
        }

        if (constructionProgress == 0f) {
            constructionProgress = 0f; // default progress = planning
        }
    }

}
