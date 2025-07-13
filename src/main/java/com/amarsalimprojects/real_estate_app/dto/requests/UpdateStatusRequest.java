package com.amarsalimprojects.real_estate_app.dto.requests;

import java.time.LocalDateTime;

import com.amarsalimprojects.real_estate_app.enums.UnitStatus;

import lombok.Data;

@Data
public class UpdateStatusRequest {

    private UnitStatus status;
    private Long ReservedBy;
    private LocalDateTime reservedDate;
    private LocalDateTime reservedUntil;

}
