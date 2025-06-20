package com.amarsalimprojects.real_estate_app.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.amarsalimprojects.real_estate_app.model.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.enums.UnitType;

import lombok.Data;

@Data
public class UnitRequest {

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
}
