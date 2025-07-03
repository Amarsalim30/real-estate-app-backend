package com.amarsalimprojects.real_estate_app.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        if (unitRequest == null) {
            throw new IllegalArgumentException("Unit request cannot be null");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Unit unit = unitMapper.toEntity(unitRequest);
        unit.setProject(project); // Set the project reference

        // Save the unit directly
        Unit savedUnit = unitRepository.save(unit);

        return savedUnit;
    }

    @Transactional
    public void removeUnitFromProject(Long projectId, Long unitId) {
        if (projectId == null || unitId == null) {
            throw new IllegalArgumentException("Project ID and Unit ID cannot be null");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unit not found"));

        if (!unit.getProject().getId().equals(projectId)) {
            throw new IllegalArgumentException("Unit does not belong to the specified project");
        }

        unitRepository.delete(unit);
    }

    @Transactional
    public Unit assignBuyerToUnit(Long unitId, Long buyerId) {
        if (unitId == null || buyerId == null) {
            throw new IllegalArgumentException("Unit ID and Buyer ID cannot be null");
        }

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Unit not found"));

        BuyerProfile buyer = buyerProfileRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));

        // Check if unit is available
        if (unit.getStatus() != UnitStatus.AVAILABLE) {
            throw new IllegalStateException("Unit is not available for assignment");
        }

        unit.setBuyer(buyer);
        unit.setStatus(UnitStatus.RESERVED);
        unit.setReservedDate(LocalDateTime.now());

        return unitRepository.save(unit);
    }

    @Transactional
    public Unit removeBuyerFromUnit(Long unitId) {
        if (unitId == null) {
            throw new IllegalArgumentException("Unit ID cannot be null");
        }

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Unit not found"));

        unit.setBuyer(null);
        unit.setStatus(UnitStatus.AVAILABLE);
        unit.setReservedDate(null);

        return unitRepository.save(unit);
    }

    @Transactional
    public Unit markUnitAsSold(Long unitId, Long buyerId) {
        if (unitId == null || buyerId == null) {
            throw new IllegalArgumentException("Unit ID and Buyer ID cannot be null");
        }

        Unit unit = unitRepository.findById(unitId)
                .orElseThrow(() -> new IllegalArgumentException("Unit not found"));

        BuyerProfile buyer = buyerProfileRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("Buyer not found"));

        unit.setBuyer(buyer);
        unit.setStatus(UnitStatus.SOLD);
        unit.setSoldDate(LocalDateTime.now());

        return unitRepository.save(unit);
    }

    @Transactional(readOnly = true)
    public List<Unit> searchUnits(UnitType type, Integer minBedrooms, Integer maxBedrooms,
            BigDecimal minPrice, BigDecimal maxPrice, UnitStatus status, Long projectId) {

        List<Unit> units = unitRepository.findAll();

        return units.stream()
                .filter(unit -> type == null || unit.getUnitType() == type)
                .filter(unit -> minBedrooms == null || (unit.getBedrooms() != null && unit.getBedrooms() >= minBedrooms))
                .filter(unit -> maxBedrooms == null || (unit.getBedrooms() != null && unit.getBedrooms() <= maxBedrooms))
                .filter(unit -> minPrice == null || (unit.getPrice() != null && unit.getPrice().compareTo(minPrice) >= 0))
                .filter(unit -> maxPrice == null || (unit.getPrice() != null && unit.getPrice().compareTo(maxPrice) <= 0))
                .filter(unit -> status == null || unit.getStatus() == status)
                .filter(unit -> projectId == null || (unit.getProject() != null && unit.getProject().getId().equals(projectId)))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Unit> getUnitsByProjectAndStatus(Long projectId, UnitStatus status) {
        if (projectId == null || status == null) {
            throw new IllegalArgumentException("Project ID and Status cannot be null");
        }
        return unitRepository.findByProjectIdAndStatus(projectId, status);
    }

    @Transactional(readOnly = true)
    public long getAvailableUnitsCountByProject(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        return unitRepository.countByProjectIdAndStatus(projectId, UnitStatus.AVAILABLE);
    }

    @Transactional(readOnly = true)
    public long getSoldUnitsCountByProject(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        return unitRepository.countByProjectIdAndStatus(projectId, UnitStatus.SOLD);
    }

    @Transactional(readOnly = true)
    public long getReservedUnitsCountByProject(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }
        return unitRepository.countByProjectIdAndStatus(projectId, UnitStatus.RESERVED);
    }

    @Transactional(readOnly = true)
    public BigDecimal getAveragePriceByProject(Long projectId) {
        if (projectId == null) {
            throw new IllegalArgumentException("Project ID cannot be null");
        }

        List<Unit> units = unitRepository.findByProjectId(projectId);
        if (units.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalPrice = units.stream()
                .filter(unit -> unit.getPrice() != null)
                .map(Unit::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long validPriceCount = units.stream()
                .filter(unit -> unit.getPrice() != null)
                .count();

        if (validPriceCount == 0) {
            return BigDecimal.ZERO;
        }

        return totalPrice.divide(BigDecimal.valueOf(validPriceCount), 2, BigDecimal.ROUND_HALF_UP);
    }

    @Transactional(readOnly = true)
    public List<Unit> getUnitsInPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        if (minPrice == null || maxPrice == null) {
            throw new IllegalArgumentException("Price range cannot be null");
        }
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Min price cannot be greater than max price");
        }
        return unitRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public void validateUnitForSale(Unit unit) {
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null");
        }
        if (unit.getStatus() == UnitStatus.SOLD) {
            throw new IllegalStateException("Unit is already sold");
        }
        if (unit.getPrice() == null || unit.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("Unit must have a valid price");
        }
    }
}
