package com.amarsalimprojects.real_estate_app.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.amarsalimprojects.real_estate_app.model.enums.ProjectStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDTO {

    private Long id;
    private String name;
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
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate expectedCompletion;
    private String developer;

    private List<String> images;
    private List<String> amenities;
    private PriceRangeDTO priceRange;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<UnitResponse> units;
}
