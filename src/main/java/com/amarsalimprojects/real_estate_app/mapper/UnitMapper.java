package com.amarsalimprojects.real_estate_app.mapper;

import com.amarsalimprojects.real_estate_app.dto.UnitDTO;
import com.amarsalimprojects.real_estate_app.model.Project;
import com.amarsalimprojects.real_estate_app.model.Unit;

public class UnitMapper {

    public static Unit toUnit(UnitDTO unitDTO) {
        Unit unit = Unit.builder()
                .id(unitDTO.getId())
                .unitNumber(unitDTO.getUnitNumber())
                .floor(unitDTO.getFloor())
                .type(unitDTO.getType())
                .bedrooms(unitDTO.getBedrooms())
                .bathrooms(unitDTO.getBathrooms())
                .sqft(unitDTO.getSqft())
                .price(unitDTO.getPrice())
                .status(unitDTO.getStatus())
                .description(unitDTO.getDescription())
                .features(unitDTO.getFeatures())
                .images(unitDTO.getImages())
                .reservedDate(unitDTO.getReservedDate())
                .soldDate(unitDTO.getSoldDate())
                .createdAt(unitDTO.getCreatedAt())
                .updatedAt(unitDTO.getUpdatedAt())
                .build();

        // You will need to fetch and assign project, reservedBy, and soldTo separately
        return unit;
    }

    public static UnitDTO toDTO(Unit unit) {
        Project project = unit.getProject();
        return UnitDTO.builder()
                .id(unit.getId())
                .projectId(project.getId())
                .unitNumber(unit.getUnitNumber())
                .floor(unit.getFloor())
                .type(unit.getType())
                .bedrooms(unit.getBedrooms())
                .bathrooms(unit.getBathrooms())
                .sqft(unit.getSqft())
                .price(unit.getPrice())
                .status(unit.getStatus())
                .description(unit.getDescription())
                .features(unit.getFeatures())
                .images(unit.getImages())
                .reservedDate(unit.getReservedDate())
                .soldDate(unit.getSoldDate())
                .createdAt(unit.getCreatedAt())
                .updatedAt(unit.getUpdatedAt())
                // You can optionally include projectId, buyer names etc. as needed
                .build();
    }

}
