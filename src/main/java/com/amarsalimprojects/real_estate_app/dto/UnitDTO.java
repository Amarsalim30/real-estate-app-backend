package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.amarsalimprojects.real_estate_app.enums.ConstructionStage;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitType;
import com.amarsalimprojects.real_estate_app.model.Unit;

public class UnitDTO {

    private Long id;
    private boolean isFeatured;
    private String unitNumber;
    private Integer floor;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer sqft;
    private String description;
    private Set<String> features;
    private List<String> images;
    private UnitStatus status;
    private UnitType unitType;
    private BigDecimal price;
    private ConstructionStage currentStage;
    private String projectName;
    private Long projectId;
    private String buyerEmail;
    private Long buyerId;
    private LocalDateTime reservedDate;
    private LocalDateTime soldDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UnitDTO() {
    }

    public UnitDTO(Unit unit) {
        this.id = unit.getId();
        this.isFeatured = unit.isFeatured();
        this.unitNumber = unit.getUnitNumber();
        this.floor = unit.getFloor();
        this.bedrooms = unit.getBedrooms();
        this.bathrooms = unit.getBathrooms();
        this.sqft = unit.getSqft();
        this.description = unit.getDescription();
        this.features = unit.getFeatures();
        this.images = unit.getImages();
        this.status = unit.getStatus();
        this.unitType = unit.getUnitType();
        this.price = unit.getPrice();
        this.currentStage = unit.getCurrentStage();
        this.reservedDate = unit.getReservedDate();
        this.soldDate = unit.getSoldDate();
        this.createdAt = unit.getCreatedAt();
        this.updatedAt = unit.getUpdatedAt();

        if (unit.getProject() != null) {
            this.projectName = unit.getProject().getName();
            this.projectId = unit.getProject().getId();
        }

        if (unit.getBuyer() != null) {
            this.buyerEmail = unit.getBuyer().getEmail();
            this.buyerId = unit.getBuyer().getId();
        }
    }

    // Builder pattern
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UnitDTO unitDTO = new UnitDTO();

        public Builder id(Long id) {
            unitDTO.id = id;
            return this;
        }

        public Builder isFeatured(boolean isFeatured) {
            unitDTO.isFeatured = isFeatured;
            return this;
        }

        public Builder unitNumber(String unitNumber) {
            unitDTO.unitNumber = unitNumber;
            return this;
        }

        public Builder floor(Integer floor) {
            unitDTO.floor = floor;
            return this;
        }

        public Builder bedrooms(Integer bedrooms) {
            unitDTO.bedrooms = bedrooms;
            return this;
        }

        public Builder bathrooms(Integer bathrooms) {
            unitDTO.bathrooms = bathrooms;
            return this;
        }

        public Builder sqft(Integer sqft) {
            unitDTO.sqft = sqft;
            return this;
        }

        public Builder description(String description) {
            unitDTO.description = description;
            return this;
        }

        public Builder features(Set<String> features) {
            unitDTO.features = features;
            return this;
        }

        public Builder images(List<String> images) {
            unitDTO.images = images;
            return this;
        }

        public Builder status(UnitStatus status) {
            unitDTO.status = status;
            return this;
        }

        public Builder type(UnitType type) {
            unitDTO.unitType = type;
            return this;
        }

        public Builder price(BigDecimal price) {
            unitDTO.price = price;
            return this;
        }

        public Builder currentStage(ConstructionStage currentStage) {
            unitDTO.currentStage = currentStage;
            return this;
        }

        public Builder projectName(String projectName) {
            unitDTO.projectName = projectName;
            return this;
        }

        public Builder projectId(Long projectId) {
            unitDTO.projectId = projectId;
            return this;
        }

        public Builder buyerEmail(String buyerEmail) {
            unitDTO.buyerEmail = buyerEmail;
            return this;
        }

        public Builder buyerId(Long buyerId) {
            unitDTO.buyerId = buyerId;
            return this;
        }

        public Builder reservedDate(LocalDateTime reservedDate) {
            unitDTO.reservedDate = reservedDate;
            return this;
        }

        public Builder soldDate(LocalDateTime soldDate) {
            unitDTO.soldDate = soldDate;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            unitDTO.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            unitDTO.updatedAt = updatedAt;
            return this;
        }

        public UnitDTO build() {
            return unitDTO;
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public String getUnitNumber() {
        return unitNumber;
    }

    public void setUnitNumber(String unitNumber) {
        this.unitNumber = unitNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(Integer bedrooms) {
        this.bedrooms = bedrooms;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public Integer getSqft() {
        return sqft;
    }

    public void setSqft(Integer sqft) {
        this.sqft = sqft;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getFeatures() {
        return features;
    }

    public void setFeatures(Set<String> features) {
        this.features = features;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public UnitStatus getStatus() {
        return status;
    }

    public void setStatus(UnitStatus status) {
        this.status = status;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setType(UnitType type) {
        this.unitType = type;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public ConstructionStage getCurrentStage() {
        return currentStage;
    }

    public void setCurrentStage(ConstructionStage currentStage) {
        this.currentStage = currentStage;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public Long getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(Long buyerId) {
        this.buyerId = buyerId;
    }

    public LocalDateTime getReservedDate() {
        return reservedDate;
    }

    public void setReservedDate(LocalDateTime reservedDate) {
        this.reservedDate = reservedDate;
    }

    public LocalDateTime getSoldDate() {
        return soldDate;
    }

    public void setSoldDate(LocalDateTime soldDate) {
        this.soldDate = soldDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
