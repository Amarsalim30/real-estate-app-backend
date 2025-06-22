package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.model.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.enums.UnitType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitDTO {

    public UnitDTO(Unit unit) {
        this.id = unit.getId();
        this.projectId = unit.getProject() != null ? unit.getProject().getId() : null;
        this.unitNumber = unit.getUnitNumber();
        this.floor = unit.getFloor();
        this.type = unit.getType();
        this.bedrooms = unit.getBedrooms();
        this.bathrooms = unit.getBathrooms();
        this.sqft = unit.getSqft();
        this.price = unit.getPrice();
        this.status = unit.getStatus();
        this.description = unit.getDescription();
        this.features = unit.getFeatures();
        this.images = unit.getImages();
        this.reservedById = unit.getReservedBy() != null ? unit.getReservedBy().getId() : null;
        this.reservedDate = unit.getReservedDate();
        this.soldToId = unit.getSoldTo() != null ? unit.getSoldTo().getId() : null;
        this.soldDate = unit.getSoldDate();
        this.createdAt = unit.getCreatedAt();
        this.updatedAt = unit.getUpdatedAt();
    }

    private Long id;
    private Long projectId;
    private String unitNumber;
    private Integer floor;
    private UnitType type;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer sqft;
    private BigDecimal price;
    private UnitStatus status;
    private String description;
    private List<String> features;
    private List<String> images;
    private Long reservedById;
    private LocalDate reservedDate;
    private Long soldToId;
    private LocalDate soldDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
