package com.amarsalimprojects.real_estate_app.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import com.amarsalimprojects.real_estate_app.enums.ConstructionStage;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitResponse {

    private Long id;
    private Long projectId;
    private String projectName;
    private String unitNumber;
    private Integer floor;
    private ConstructionStage currentStage;
    private UnitType unitType;
    private Integer bedrooms;
    private Integer bathrooms;
    private boolean featured;
    private Integer sqft;
    private BigDecimal price;
    private UnitStatus status;
    private String description;
    private Set<String> features;
    private List<String> images;
    private Long buyerId;
    private String buyerEmail;
    private LocalDate soldDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime reservedDate;
    private Long reservedBy;
}
