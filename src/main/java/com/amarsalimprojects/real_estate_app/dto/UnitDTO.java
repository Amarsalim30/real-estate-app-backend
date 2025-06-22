package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.model.enums.ConstructionStage;
import com.amarsalimprojects.real_estate_app.model.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.enums.UnitType;

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
    private UnitType type;
    private BigDecimal price;
    private ConstructionStage currentStage;
    private String projectName;
    private Long projectId;
    private String buyerEmail;
    private Long buyerId;

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
        this.type = unit.getType();
        this.price = unit.getPrice();
        this.currentStage = unit.getCurrentStage();

        if (unit.getProject() != null) {
            this.projectName = unit.getProject().getName();
            this.projectId = unit.getProject().getId();
        }

        if (unit.getBuyer() != null) {
            this.buyerEmail = unit.getBuyer().getEmail();
            this.buyerId = unit.getBuyer().getId();
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

    public UnitType getType() {
        return type;
    }

    public void setType(UnitType type) {
        this.type = type;
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
}
