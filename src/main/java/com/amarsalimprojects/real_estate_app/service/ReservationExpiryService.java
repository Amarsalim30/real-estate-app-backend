package com.amarsalimprojects.real_estate_app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.repository.UnitRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationExpiryService {

    private final UnitRepository unitRepository;

    @Scheduled(fixedRate = 60000) // Runs every minute
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Unit> expiredUnits = unitRepository.findByStatusAndReservedUntilBefore(UnitStatus.RESERVED, now);

        for (Unit unit : expiredUnits) {
            unit.setStatus(UnitStatus.AVAILABLE);
            unit.setReservedBy(null);
            unit.setReservedDate(null);
            unit.setReservedUntil(null);
        }

        unitRepository.saveAll(expiredUnits);
    }
}
