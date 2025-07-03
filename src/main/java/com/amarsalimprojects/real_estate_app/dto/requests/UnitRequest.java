package com.amarsalimprojects.real_estate_app.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.amarsalimprojects.real_estate_app.enums.ConstructionStage;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UnitRequest {

    private Long projectId;
    private Long unitId;
    private String unitNumber;
    private Integer floor;
    private ConstructionStage constructionStage;
    private UnitType unitType;
    private Integer bedrooms;
    private Integer bathrooms;
    private boolean isFeatured;
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
}
