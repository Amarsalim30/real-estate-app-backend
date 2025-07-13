package com.amarsalimprojects.real_estate_app.mapper;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.amarsalimprojects.real_estate_app.dto.requests.UnitRequest;
import com.amarsalimprojects.real_estate_app.dto.responses.UnitResponse;
import com.amarsalimprojects.real_estate_app.model.Unit;

@Component
public class UnitMapper {

    public UnitResponse toResponse(Unit unit) {
        if (unit == null) {
            return null;
        }

        return UnitResponse.builder()
                .id(unit.getId())
                .projectId(unit.getProject() != null ? unit.getProject().getId() : null)
                .projectName(unit.getProject() != null ? unit.getProject().getName() : null)
                .unitNumber(unit.getUnitNumber())
                .floor(unit.getFloor())
                .bedrooms(unit.getBedrooms())
                .bathrooms(unit.getBathrooms())
                .sqft(unit.getSqft())
                .description(unit.getDescription())
                .features(unit.getFeatures())
                .images(unit.getImages())
                .status(unit.getStatus())
                .unitType(unit.getUnitType())
                .price(unit.getPrice())
                .reservedDate(unit.getReservedDate() != null ? unit.getReservedDate() : null)
                .reservedBy(unit.getReservedBy() != null ? unit.getReservedBy() : null)
                .soldDate(unit.getSoldDate() != null ? unit.getSoldDate().toLocalDate() : null)
                .currentStage(unit.getCurrentStage())
                .featured(unit.isFeatured())
                .buyerId(unit.getBuyer() != null ? unit.getBuyer().getId() : null)
                .buyerEmail(unit.getBuyer() != null ? unit.getBuyer().getEmail() : null)
                .createdAt(unit.getCreatedAt())
                .updatedAt(unit.getUpdatedAt())
                .build();
    }

    public Unit toEntity(UnitRequest request) {
        if (request == null) {
            return null;
        }

        validateUnitRequest(request);

        return Unit.builder()
                .unitNumber(request.getUnitNumber())
                .floor(request.getFloor())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .sqft(request.getSqft())
                .description(request.getDescription())
                .features(request.getFeatures() != null ? new HashSet<>(request.getFeatures()) : new HashSet<>())
                .images(request.getImages())
                .status(request.getStatus())
                .unitType(request.getUnitType())
                .price(request.getPrice())
                .currentStage(request.getConstructionStage())
                .featured(request.isFeatured())
                .reservedDate(request.getReservedDate() != null ? request.getReservedDate().atStartOfDay() : null)
                .soldDate(request.getSoldDate() != null ? request.getSoldDate().atStartOfDay() : null)
                .build();
    }

    public void updateEntityFromRequest(Unit unit, UnitRequest request) {
        if (unit == null || request == null) {
            return;
        }

        validateUnitRequest(request);

        if (request.getUnitNumber() != null) {
            unit.setUnitNumber(request.getUnitNumber());
        }
        if (request.getFloor() != null) {
            unit.setFloor(request.getFloor());
        }
        if (request.getBedrooms() != null) {
            unit.setBedrooms(request.getBedrooms());
        }
        if (request.getBathrooms() != null) {
            unit.setBathrooms(request.getBathrooms());
        }
        if (request.getSqft() != null) {
            unit.setSqft(request.getSqft());
        }
        if (request.getDescription() != null) {
            unit.setDescription(request.getDescription());
        }
        if (request.getFeatures() != null) {
            unit.setFeatures(new HashSet<>(request.getFeatures()));
        }
        if (request.getImages() != null) {
            unit.setImages(request.getImages());
        }
        if (request.getStatus() != null) {
            unit.setStatus(request.getStatus());
        }
        if (request.getUnitType() != null) {
            unit.setUnitType(request.getUnitType());
        }
        if (request.getPrice() != null) {
            unit.setPrice(request.getPrice());
        }
        if (request.getConstructionStage() != null) {
            unit.setCurrentStage(request.getConstructionStage());
        }

        // Fix: Use setFeatured instead of setIsFeatured
        unit.setFeatured(request.isFeatured());

        // Fix: Handle null values for dates
        if (request.getReservedDate() != null) {
            unit.setReservedDate(request.getReservedDate().atStartOfDay());
        }
        if (request.getSoldDate() != null) {
            unit.setSoldDate(request.getSoldDate().atStartOfDay());
        }
    }

    public List<UnitResponse> toResponseList(List<Unit> units) {
        if (units == null) {
            return List.of(); // Return empty list instead of null
        }
        return units.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void validateUnitRequest(UnitRequest request) {
        if (request.getBedrooms() != null && request.getBedrooms() < 0) {
            throw new IllegalArgumentException("Bedrooms cannot be negative");
        }
        if (request.getBathrooms() != null && request.getBathrooms() < 0) {
            throw new IllegalArgumentException("Bathrooms cannot be negative");
        }
        if (request.getSqft() != null && request.getSqft() <= 0) {
            throw new IllegalArgumentException("Square footage must be positive");
        }
        if (request.getPrice() != null && request.getPrice().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
