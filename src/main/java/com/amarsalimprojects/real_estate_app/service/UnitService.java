package com.amarsalimprojects.real_estate_app.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.amarsalimprojects.real_estate_app.dto.requests.UnitRequest;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitType;
import com.amarsalimprojects.real_estate_app.mapper.UnitMapper;
import com.amarsalimprojects.real_estate_app.model.BuyerProfile;
import com.amarsalimprojects.real_estate_app.model.Project;
import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.repository.BuyerProfileRepository;
import com.amarsalimprojects.real_estate_app.repository.ProjectRepository;
import com.amarsalimprojects.real_estate_app.repository.UnitRepository;

@Service
@Transactional
public class UnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UnitMapper unitMapper;

    @Autowired
    private BuyerProfileRepository buyerProfileRepository;

    @Transactional
    public Unit addUnitToProject(Long projectId, UnitRequest unitRequest) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
        Unit unit = unitMapper.toEntity(unitRequest);

        project.addUnit(unit); // Bi-directional
        projectRepository.save(project); // Persists everything due to cascade

        return unit;
    }

    @Transactional
    public void removeUnitFromProject(Long projectId, Long unitId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unit not found"));

        project.removeUnit(unit); // remove from list and break reference
        projectRepository.save(project); // `orphanRemoval = true` deletes unit
    }

    @Transactional
    public Unit assignBuyerToUnit(Long unitId, Long buyerId) {
        Optional<Unit> unitOpt = unitRepository.findById(unitId);
        Optional<BuyerProfile> buyerOpt = buyerProfileRepository.findById(buyerId);

        if (unitOpt.isPresent() && buyerOpt.isPresent()) {
            Unit unit = unitOpt.get();
            BuyerProfile buyer = buyerOpt.get();

            // Check if unit is available
            if (unit.getStatus() != UnitStatus.AVAILABLE) {
                throw new IllegalStateException("Unit is not available for assignment");
            }

            unit.setBuyer(buyer);
            unit.setStatus(UnitStatus.RESERVED);

            return unitRepository.save(unit);
        } else {
            throw new IllegalArgumentException("Unit or Buyer not found");
        }
    }

    public Unit removeBuyerFromUnit(Long unitId) {
        Optional<Unit> unitOpt = unitRepository.findById(unitId);

        if (unitOpt.isPresent()) {
            Unit unit = unitOpt.get();
            unit.setBuyer(null);
            unit.setStatus(UnitStatus.AVAILABLE);
            return unitRepository.save(unit);
        } else {
            throw new IllegalArgumentException("Unit not found");
        }
    }

    public Unit markUnitAsSold(Long unitId, Long buyerId) {
        Optional<Unit> unitOpt = unitRepository.findById(unitId);
        Optional<BuyerProfile> buyerOpt = buyerProfileRepository.findById(buyerId);

        if (unitOpt.isPresent() && buyerOpt.isPresent()) {
            Unit unit = unitOpt.get();
            BuyerProfile buyer = buyerOpt.get();

            unit.setBuyer(buyer);
            unit.setStatus(UnitStatus.SOLD);

            return unitRepository.save(unit);
        } else {
            throw new IllegalArgumentException("Unit or Buyer not found");
        }
    }

    public List<Unit> searchUnits(UnitType type, Integer minBedrooms, Integer maxBedrooms,
            BigDecimal minPrice, BigDecimal maxPrice, UnitStatus status, Long projectId) {
        List<Unit> units = unitRepository.findAll();

        return units.stream()
                .filter(unit -> type == null || unit.getUnitType() == type)
                .filter(unit -> minBedrooms == null || unit.getBedrooms() >= minBedrooms)
                .filter(unit -> maxBedrooms == null || unit.getBedrooms() <= maxBedrooms)
                .filter(unit -> minPrice == null || unit.getPrice().compareTo(minPrice) >= 0)
                .filter(unit -> maxPrice == null || unit.getPrice().compareTo(maxPrice) <= 0)
                .filter(unit -> status == null || unit.getStatus() == status)
                .filter(unit -> projectId == null || unit.getProject().getId().equals(projectId))
                .collect(Collectors.toList());
    }

    public List<Unit> getUnitsByProjectAndStatus(Long projectId, UnitStatus status) {
        return unitRepository.findByProjectIdAndStatus(projectId, status);
    }

    public long getAvailableUnitsCountByProject(Long projectId) {
        return unitRepository.countByProjectIdAndStatus(projectId, UnitStatus.AVAILABLE);
    }

    public long getSoldUnitsCountByProject(Long projectId) {
        return unitRepository.countByProjectIdAndStatus(projectId, UnitStatus.SOLD);
    }

    public long getReservedUnitsCountByProject(Long projectId) {
        return unitRepository.countByProjectIdAndStatus(projectId, UnitStatus.RESERVED);
    }

    public BigDecimal getAveragePriceByProject(Long projectId) {
        List<Unit> units = unitRepository.findByProjectId(projectId);
        if (units.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return units.stream()
                .map(Unit::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(units.size()), java.math.RoundingMode.HALF_UP);
    }

    public List<Unit> getUnitsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return unitRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public void validateUnitForSale(Unit unit) {
        if (unit.getStatus() == UnitStatus.SOLD) {
            throw new IllegalStateException("Unit is already sold");
        }
        if (unit.getPrice() == null || unit.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Unit must have a valid price");
        }
    }
}
