package com.amarsalimprojects.real_estate_app.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amarsalimprojects.real_estate_app.dto.requests.UnitRequest;
import com.amarsalimprojects.real_estate_app.dto.responses.UnitResponse;
import com.amarsalimprojects.real_estate_app.enums.ConstructionStage;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitType;
import com.amarsalimprojects.real_estate_app.mapper.UnitMapper;
import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.repository.UnitRepository;
import com.amarsalimprojects.real_estate_app.service.UnitService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/units")
@CrossOrigin(origins = "*")
public class UnitController {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private UnitService unitService;

    @Autowired
    private UnitMapper unitMapper;

    // CREATE - Add a new unit
    @PostMapping
    public ResponseEntity<UnitResponse> createUnit(@Valid @RequestBody UnitRequest request) {
        try {
            // Validate required fields
            if (request.getProjectId() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Check for duplicate unitNumber using the new method
            if (request.getUnitNumber() != null && !request.getUnitNumber().isEmpty()) {
                if (unitRepository.existsByUnitNumber(request.getUnitNumber())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }
            }

            // Set defaults
            if (request.getStatus() == null) {
                request.setStatus(UnitStatus.AVAILABLE);
            }
            if (request.getConstructionStage() == null) {
                request.setConstructionStage(ConstructionStage.PLANNING);
            }
            if (request.getUnitType() == null) {
                request.setUnitType(UnitType.APARTMENT);
            }
            if (request.getPrice() == null) {
                request.setPrice(BigDecimal.ZERO);
            }
            if (request.getBedrooms() == null) {
                request.setBedrooms(0);
            }
            if (request.getBathrooms() == null) {
                request.setBathrooms(0);
            }
            if (request.getSqft() == null) {
                request.setSqft(0);
            }
            if (request.getUnitNumber() == null || request.getUnitNumber().isEmpty()) {
                request.setUnitNumber("UN" + System.currentTimeMillis());
            }

            // Save unit
            Unit savedUnit = unitService.addUnitToProject(request.getProjectId(), request);

            // Map entity to response DTO
            UnitResponse response = unitMapper.toResponse(savedUnit);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            e.printStackTrace(); // For debugging - consider using proper logging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get all units as Response DTOs
    @GetMapping("/all")
    public ResponseEntity<List<UnitResponse>> getAllUnits() {
        try {
            List<Unit> units = unitRepository.findAll();
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }

            List<UnitResponse> unitResponses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(unitResponses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get all units (raw entities) - Keep for backward compatibility
    @GetMapping
    public ResponseEntity<List<Unit>> getAllUnitsRaw() {
        try {
            List<Unit> units = unitRepository.findAll();
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(units);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get unit by ID
    @GetMapping("/{id}")
    public ResponseEntity<UnitResponse> getUnitById(@PathVariable("id") Long id) {
        try {
            Optional<Unit> unit = unitRepository.findById(id);
            if (unit.isPresent()) {
                UnitResponse response = unitMapper.toResponse(unit.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get unit by unit number
    @GetMapping("/unit-number/{unitNumber}")
    public ResponseEntity<UnitResponse> getUnitByUnitNumber(@PathVariable("unitNumber") String unitNumber) {
        try {
            Optional<Unit> unit = unitRepository.findByUnitNumber(unitNumber);
            if (unit.isPresent()) {
                UnitResponse response = unitMapper.toResponse(unit.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get units by project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<UnitResponse>> getUnitsByProjectId(@PathVariable("projectId") Long projectId) {
        try {
            List<Unit> units = unitRepository.findByProjectId(projectId);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get units by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UnitResponse>> getUnitsByStatus(@PathVariable("status") UnitStatus status) {
        try {
            List<Unit> units = unitRepository.findByStatus(status);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get units by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<UnitResponse>> getUnitsByType(@PathVariable("type") UnitType type) {
        try {
            List<Unit> units = unitRepository.findByUnitType(type);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get units by floor
    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<UnitResponse>> getUnitsByFloor(@PathVariable("floor") Integer floor) {
        try {
            List<Unit> units = unitRepository.findByFloor(floor);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get units by bedrooms
    @GetMapping("/bedrooms/{bedrooms}")
    public ResponseEntity<List<UnitResponse>> getUnitsByBedrooms(@PathVariable("bedrooms") Integer bedrooms) {
        try {
            List<Unit> units = unitRepository.findByBedrooms(bedrooms);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get units by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<UnitResponse>> getUnitsByPriceRange(
            @RequestParam("minPrice") BigDecimal minPrice,
            @RequestParam("maxPrice") BigDecimal maxPrice) {
        try {
            if (minPrice.compareTo(maxPrice) > 0) {
                return ResponseEntity.badRequest().build();
            }
            List<Unit> units = unitRepository.findByPriceBetween(minPrice, maxPrice);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get units by buyer ID
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<UnitResponse>> getUnitsByBuyerId(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Unit> units = unitRepository.findByBuyerId(buyerId);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get featured units - FIX: Change method name
    @GetMapping("/featured")
    public ResponseEntity<List<UnitResponse>> getFeaturedUnits() {
        try {
            List<Unit> units = unitRepository.findByFeaturedTrue(); // Fixed method name
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Update unit by ID
    @PutMapping("/{id}")
    public ResponseEntity<UnitResponse> updateUnit(@PathVariable("id") Long id, @Valid @RequestBody UnitRequest request) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unitToUpdate = existingUnit.get();
                unitMapper.updateEntityFromRequest(unitToUpdate, request);
                Unit updatedUnit = unitRepository.save(unitToUpdate);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Partial update of unit
    @PatchMapping("/{id}")
    public ResponseEntity<UnitResponse> partialUpdateUnit(@PathVariable("id") Long id, @RequestBody UnitRequest request) {
        try {
            Optional<Unit> existingUnitOpt = unitRepository.findById(id);
            if (existingUnitOpt.isPresent()) {
                Unit existingUnit = existingUnitOpt.get();
                unitMapper.updateEntityFromRequest(existingUnit, request);
                Unit updatedUnit = unitRepository.save(existingUnit);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Update unit status
    @PatchMapping("/{id}/status")
    public ResponseEntity<UnitResponse> updateUnitStatus(@PathVariable("id") Long id, @RequestParam("status") UnitStatus status) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setStatus(status);
                Unit updatedUnit = unitRepository.save(unit);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Update construction stage
    @PatchMapping("/{id}/construction-stage")
    public ResponseEntity<UnitResponse> updateConstructionStage(@PathVariable("id") Long id, @RequestParam("stage") ConstructionStage stage) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setCurrentStage(stage);
                Unit updatedUnit = unitRepository.save(unit);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Assign buyer to unit
    @PatchMapping("/{id}/assign-buyer/{buyerId}")
    public ResponseEntity<UnitResponse> assignBuyerToUnit(@PathVariable("id") Long id, @PathVariable("buyerId") Long buyerId) {
        try {
            Unit updatedUnit = unitService.assignBuyerToUnit(id, buyerId);
            UnitResponse response = unitMapper.toResponse(updatedUnit);
            return ResponseEntity.ok(response);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Remove buyer from unit
    @PatchMapping("/{id}/remove-buyer")
    public ResponseEntity<UnitResponse> removeBuyerFromUnit(@PathVariable("id") Long id) {
        try {
            Unit updatedUnit = unitService.removeBuyerFromUnit(id);
            UnitResponse response = unitMapper.toResponse(updatedUnit);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Toggle featured status
    @PatchMapping("/{id}/toggle-featured")
    public ResponseEntity<UnitResponse> toggleFeaturedStatus(@PathVariable("id") Long id) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setFeatured(!unit.isFeatured());
                Unit updatedUnit = unitRepository.save(unit);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Add feature to unit
    @PatchMapping("/{id}/add-feature")
    public ResponseEntity<UnitResponse> addFeatureToUnit(@PathVariable("id") Long id, @RequestParam("feature") String feature) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getFeatures() != null) {
                    unit.getFeatures().add(feature);
                } else {
                    unit.setFeatures(Set.of(feature)); // Fix: Import Set
                }
                Unit updatedUnit = unitRepository.save(unit);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // UPDATE - Remove feature from unit
    @PatchMapping("/{id}/remove-feature")
    public ResponseEntity<UnitResponse> removeFeatureFromUnit(@PathVariable("id") Long id, @RequestParam("feature") String feature) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getFeatures() != null) {
                    unit.getFeatures().remove(feature);
                }
                Unit updatedUnit = unitRepository.save(unit);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Delete unit by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUnit(@PathVariable("id") Long id) {
        try {
            if (unitRepository.existsById(id)) {
                unitRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE - Delete units by project ID
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<Void> deleteUnitsByProjectId(@PathVariable("projectId") Long projectId) {
        try {
            List<Unit> units = unitRepository.findByProjectId(projectId);
            if (!units.isEmpty()) {
                unitRepository.deleteAll(units);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Utility endpoints - Essential only
    @GetMapping("/count")
    public ResponseEntity<Long> getUnitCount() {
        try {
            long count = unitRepository.count();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<UnitResponse>> getAvailableUnits() {
        try {
            List<Unit> units = unitRepository.findByStatus(UnitStatus.AVAILABLE);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UnitResponse>> searchUnits(
            @RequestParam(required = false) UnitType type,
            @RequestParam(required = false) Integer minBedrooms,
            @RequestParam(required = false) Integer maxBedrooms,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) UnitStatus status,
            @RequestParam(required = false) Long projectId) {
        try {
            List<Unit> units = unitService.searchUnits(type, minBedrooms, maxBedrooms, minPrice, maxPrice, status, projectId);
            if (units.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
