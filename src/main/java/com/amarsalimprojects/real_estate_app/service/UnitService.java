package com.amarsalimprojects.real_estate_app.service;

import org.springframework.stereotype.Service;

import com.amarsalimprojects.real_estate_app.model.Project;
import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.repository.ProjectRepository;
import com.amarsalimprojects.real_estate_app.repository.UnitRepository;
import com.amarsalimprojects.real_estate_app.requests.UnitRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UnitService {

    private final UnitRepository unitRepository;
    private final ProjectRepository projectRepository;

    @Transactional
    public Unit createUnit(UnitRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Unit unit = Unit.builder()
                .unitNumber(request.getUnitNumber())
                .floor(request.getFloor())
                .type(request.getType())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .sqft(request.getSqft())
                .price(request.getPrice())
                .status(request.getStatus())
                .description(request.getDescription())
                .features(request.getFeatures())
                .images(request.getImages())
                .reservedDate(request.getReservedDate())
                .soldDate(request.getSoldDate())
                .build();
        project.addUnit(unit);
        return unitRepository.save(unit); // ✅ Just save the unit

    }
}
