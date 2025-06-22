package com.amarsalimprojects.real_estate_app.dto.requests;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitResponse {

    private Long id;
    private String unitNumber;
    private Integer floor;
    private UnitType type;
    private Integer bedrooms;
    private Integer bathrooms;
    private Integer sqft;
    private BigDecimal price;
    private UnitStatus status;
    private String description;

    private LocalDate reservedDate;
    private LocalDate soldDate;
}
