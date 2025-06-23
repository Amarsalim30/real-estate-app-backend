package com.amarsalimprojects.real_estate_app.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<UnitResponse> createUnit(@RequestBody UnitRequest request) {
        try {
            // Validate required fields
            if (request.getProjectId() == null) {
                return ResponseEntity.badRequest().build();
            }

            // Check for duplicate unitNumber
            if (request.getUnitNumber() != null && !request.getUnitNumber().isEmpty()) {
                Optional<Unit> existingUnit = unitRepository.findByUnitNumber(request.getUnitNumber());
                if (existingUnit.isPresent()) {
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

        } catch (Exception e) {
            e.printStackTrace(); // Debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // READ - Get all units as Response DTOs
    @GetMapping("/all")
    public ResponseEntity<List<UnitResponse>> getAllUnits() {
        try {
            List<Unit> units = unitRepository.findAll();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<UnitResponse> unitResponses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(unitResponses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all units (raw entities) - Keep for backward compatibility
    @GetMapping
    public ResponseEntity<List<Unit>> getAllUnitsRaw() {
        try {
            List<Unit> units = unitRepository.findAll();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get unit by ID
    @GetMapping("/{id}")
    public ResponseEntity<UnitResponse> getUnitById(@PathVariable("id") Long id) {
        try {
            Optional<Unit> unit = unitRepository.findById(id);
            if (unit.isPresent()) {
                UnitResponse response = unitMapper.toResponse(unit.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get unit by unit number
    @GetMapping("/unit-number/{unitNumber}")
    public ResponseEntity<UnitResponse> getUnitByUnitNumber(@PathVariable("unitNumber") String unitNumber) {
        try {
            Optional<Unit> unit = unitRepository.findByUnitNumber(unitNumber);
            if (unit.isPresent()) {
                UnitResponse response = unitMapper.toResponse(unit.get());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<UnitResponse>> getUnitsByProjectId(@PathVariable("projectId") Long projectId) {
        try {
            List<Unit> units = unitRepository.findByProjectId(projectId);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UnitResponse>> getUnitsByStatus(@PathVariable("status") UnitStatus status) {
        try {
            List<Unit> units = unitRepository.findByStatus(status);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<UnitResponse>> getUnitsByType(@PathVariable("type") UnitType type) {
        try {
            List<Unit> units = unitRepository.findByUnitType(type);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by floor
    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<UnitResponse>> getUnitsByFloor(@PathVariable("floor") Integer floor) {
        try {
            List<Unit> units = unitRepository.findByFloor(floor);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by bedrooms
    @GetMapping("/bedrooms/{bedrooms}")
    public ResponseEntity<List<UnitResponse>> getUnitsByBedrooms(@PathVariable("bedrooms") Integer bedrooms) {
        try {
            List<Unit> units = unitRepository.findByBedrooms(bedrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<UnitResponse>> getUnitsByPriceRange(
            @RequestParam("minPrice") BigDecimal minPrice,
            @RequestParam("maxPrice") BigDecimal maxPrice) {
        try {
            List<Unit> units = unitRepository.findByPriceBetween(minPrice, maxPrice);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by buyer ID
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<UnitResponse>> getUnitsByBuyerId(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Unit> units = unitRepository.findByBuyerId(buyerId);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get featured units
    @GetMapping("/featured")
    public ResponseEntity<List<UnitResponse>> getFeaturedUnits() {
        try {
            List<Unit> units = unitRepository.findByIsFeaturedTrue();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update unit by ID
    @PutMapping("/{id}")
    public ResponseEntity<UnitResponse> updateUnit(@PathVariable("id") Long id, @RequestBody UnitRequest request) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unitToUpdate = existingUnit.get();
                unitMapper.updateEntityFromRequest(unitToUpdate, request);
                Unit updatedUnit = unitRepository.save(unitToUpdate);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Assign buyer to unit
    @PatchMapping("/{id}/assign-buyer/{buyerId}")
    public ResponseEntity<UnitResponse> assignBuyerToUnit(@PathVariable("id") Long id, @PathVariable("buyerId") Long buyerId) {
        try {
            Unit updatedUnit = unitService.assignBuyerToUnit(id, buyerId);
            UnitResponse response = unitMapper.toResponse(updatedUnit);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Remove buyer from unit
    @PatchMapping("/{id}/remove-buyer")
    public ResponseEntity<UnitResponse> removeBuyerFromUnit(@PathVariable("id") Long id) {
        try {
            Unit updatedUnit = unitService.removeBuyerFromUnit(id);
            UnitResponse response = unitMapper.toResponse(updatedUnit);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Toggle featured status
    @PatchMapping("/{id}/toggle-featured")
    public ResponseEntity<UnitResponse> toggleFeaturedStatus(@PathVariable("id") Long id) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setIsFeatured(!unit.isFeatured());
                Unit updatedUnit = unitRepository.save(unit);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
                }
                Unit updatedUnit = unitRepository.save(unit);
                UnitResponse response = unitMapper.toResponse(updatedUnit);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete unit by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUnit(@PathVariable("id") Long id) {
        try {
            Optional<Unit> unit = unitRepository.findById(id);
            if (unit.isPresent()) {
                unitRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete units by project ID
    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<HttpStatus> deleteUnitsByProjectId(@PathVariable("projectId") Long projectId) {
        try {
            List<Unit> units = unitRepository.findByProjectId(projectId);
            if (!units.isEmpty()) {
                unitRepository.deleteAll(units);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Utility endpoints
    // GET - Get unit count
    @GetMapping("/count")
    public ResponseEntity<Long> getUnitCount() {
        try {
            long count = unitRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get unit count by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getUnitCountByStatus(@PathVariable("status") UnitStatus status) {
        try {
            List<Unit> units = unitRepository.findByStatus(status);
            return new ResponseEntity<>((long) units.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get unit count by project
    @GetMapping("/count/project/{projectId}")
    public ResponseEntity<Long> getUnitCountByProject(@PathVariable("projectId") Long projectId) {
        try {
            List<Unit> units = unitRepository.findByProjectId(projectId);
            return new ResponseEntity<>((long) units.size(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get available units
    @GetMapping("/available")
    public ResponseEntity<List<UnitResponse>> getAvailableUnits() {
        try {
            List<Unit> units = unitRepository.findByStatus(UnitStatus.AVAILABLE);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get sold units
    @GetMapping("/sold")
    public ResponseEntity<List<UnitResponse>> getSoldUnits() {
        try {
            List<Unit> units = unitRepository.findByStatus(UnitStatus.SOLD);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get reserved units
    @GetMapping("/reserved")
    public ResponseEntity<List<UnitResponse>> getReservedUnits() {
        try {
            List<Unit> units = unitRepository.findByStatus(UnitStatus.RESERVED);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Search units by multiple criteria
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
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get units by construction stage
    @GetMapping("/construction-stage/{stage}")
    public ResponseEntity<List<UnitResponse>> getUnitsByConstructionStage(@PathVariable ConstructionStage stage) {
        try {
            List<Unit> units = unitRepository.findByCurrentStage(stage);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<UnitResponse> responses = unitMapper.toResponseList(units);
            return new ResponseEntity<>(responses, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PATCH - Bulk update unit status
    @PatchMapping("/bulk-update-status")
    public ResponseEntity<List<UnitResponse>> bulkUpdateUnitStatus(
            @RequestParam List<Long> unitIds,
            @RequestParam UnitStatus status) {
        try {
            List<Unit> updatedUnits = unitIds.stream()
                    .map(id -> unitRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(unit -> unit.setStatus(status))
                    .map(unitRepository::save)
                    .collect(Collectors.toList());

            List<UnitResponse> responses = unitMapper.toResponseList(updatedUnits);
            return ResponseEntity.ok(responses);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get similar units (same type, similar price range)
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<UnitResponse>> getSimilarUnits(@PathVariable Long id) {
        try {
            Optional<Unit> unitOpt = unitRepository.findById(id);
            if (unitOpt.isPresent()) {
                Unit unit = unitOpt.get();
                BigDecimal priceRange = unit.getPrice().multiply(BigDecimal.valueOf(0.2)); // 20% range
                BigDecimal minPrice = unit.getPrice().subtract(priceRange);
                BigDecimal maxPrice = unit.getPrice().add(priceRange);

                List<Unit> similarUnits = unitRepository.findAll().stream()
                        .filter(u -> !u.getId().equals(id))
                        .filter(u -> u.getUnitType() == unit.getUnitType())
                        .filter(u -> u.getPrice().compareTo(minPrice) >= 0 && u.getPrice().compareTo(maxPrice) <= 0)
                        .limit(5)
                        .collect(Collectors.toList());

                List<UnitResponse> responses = unitMapper.toResponseList(similarUnits);
                return ResponseEntity.ok(responses);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
