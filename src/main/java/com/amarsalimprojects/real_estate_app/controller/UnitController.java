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

import com.amarsalimprojects.real_estate_app.dto.UnitDTO;
import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.model.enums.ConstructionStage;
import com.amarsalimprojects.real_estate_app.model.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.enums.UnitType;
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

    // CREATE - Add a new unit
    @PostMapping
    public ResponseEntity<Unit> createUnit(@RequestBody Unit unit) {
        try {
            // Check if unit number already exists
            if (unit.getUnitNumber() != null && !unit.getUnitNumber().isEmpty()) {
                Optional<Unit> existingUnit = unitRepository.findByUnitNumber(unit.getUnitNumber());
                if (existingUnit.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }

            // Set default values if not provided
            if (unit.getStatus() == null) {
                unit.setStatus(UnitStatus.AVAILABLE);
            }
            if (unit.getCurrentStage() == null) {
                unit.setCurrentStage(ConstructionStage.PLANNING);
            }

            Unit savedUnit = unitRepository.save(unit);
            return new ResponseEntity<>(savedUnit, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all units as DTOs
    @GetMapping("/all")
    public ResponseEntity<List<UnitDTO>> getAllUnits() {
        try {
            List<Unit> units = unitRepository.findAll();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<UnitDTO> unitDTOs = units.stream()
                    .map(unit -> new UnitDTO(unit))
                    .collect(Collectors.toList());

            return new ResponseEntity<>(unitDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all units (raw entities)
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
    public ResponseEntity<Unit> getUnitById(@PathVariable("id") Long id) {
        try {
            Optional<Unit> unit = unitRepository.findById(id);
            if (unit.isPresent()) {
                return new ResponseEntity<>(unit.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get unit by unit number
    @GetMapping("/unit-number/{unitNumber}")
    public ResponseEntity<Unit> getUnitByUnitNumber(@PathVariable("unitNumber") String unitNumber) {
        try {
            Optional<Unit> unit = unitRepository.findByUnitNumber(unitNumber);
            if (unit.isPresent()) {
                return new ResponseEntity<>(unit.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by project ID
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<Unit>> getUnitsByProjectId(@PathVariable("projectId") Long projectId) {
        try {
            List<Unit> units = unitRepository.findByProjectId(projectId);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Unit>> getUnitsByStatus(@PathVariable("status") UnitStatus status) {
        try {
            List<Unit> units = unitRepository.findByStatus(status);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by type
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Unit>> getUnitsByType(@PathVariable("type") UnitType type) {
        try {
            List<Unit> units = unitRepository.findByType(type);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by floor
    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<Unit>> getUnitsByFloor(@PathVariable("floor") Integer floor) {
        try {
            List<Unit> units = unitRepository.findByFloor(floor);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by bedrooms
    @GetMapping("/bedrooms/{bedrooms}")
    public ResponseEntity<List<Unit>> getUnitsByBedrooms(@PathVariable("bedrooms") Integer bedrooms) {
        try {
            List<Unit> units = unitRepository.findByBedrooms(bedrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<Unit>> getUnitsByPriceRange(
            @RequestParam("minPrice") BigDecimal minPrice,
            @RequestParam("maxPrice") BigDecimal maxPrice) {
        try {
            List<Unit> units = unitRepository.findByPriceBetween(minPrice, maxPrice);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by buyer ID
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<Unit>> getUnitsByBuyerId(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Unit> units = unitRepository.findByBuyerId(buyerId);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get featured units
    @GetMapping("/featured")
    public ResponseEntity<List<Unit>> getFeaturedUnits() {
        try {
            List<Unit> units = unitRepository.findByIsFeaturedTrue();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update unit by ID
    @PutMapping("/{id}")
    public ResponseEntity<Unit> updateUnit(@PathVariable("id") Long id, @RequestBody Unit unit) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unitToUpdate = existingUnit.get();

                // Update fields that exist in the model
                unitToUpdate.setIsFeatured(unit.isFeatured());
                unitToUpdate.setUnitNumber(unit.getUnitNumber());
                unitToUpdate.setFloor(unit.getFloor());
                unitToUpdate.setBedrooms(unit.getBedrooms());
                unitToUpdate.setBathrooms(unit.getBathrooms());
                unitToUpdate.setSqft(unit.getSqft());
                unitToUpdate.setDescription(unit.getDescription());
                unitToUpdate.setFeatures(unit.getFeatures());
                unitToUpdate.setImages(unit.getImages());
                unitToUpdate.setStatus(unit.getStatus());
                unitToUpdate.setType(unit.getType());
                unitToUpdate.setPrice(unit.getPrice());
                unitToUpdate.setCurrentStage(unit.getCurrentStage());
                unitToUpdate.setProject(unit.getProject());
                unitToUpdate.setBuyer(unit.getBuyer());

                Unit updatedUnit = unitRepository.save(unitToUpdate);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Partial update of unit
    @PatchMapping("/{id}")
    public ResponseEntity<Unit> partialUpdateUnit(@PathVariable("id") Long id, @RequestBody Unit unitUpdates) {
        try {
            Optional<Unit> existingUnitOpt = unitRepository.findById(id);
            if (existingUnitOpt.isPresent()) {
                Unit existingUnit = existingUnitOpt.get();

                // Update only non-null fields
                if (unitUpdates.getUnitNumber() != null) {
                    existingUnit.setUnitNumber(unitUpdates.getUnitNumber());
                }
                if (unitUpdates.getFloor() != null) {
                    existingUnit.setFloor(unitUpdates.getFloor());
                }
                if (unitUpdates.getType() != null) {
                    existingUnit.setType(unitUpdates.getType());
                }
                if (unitUpdates.getBedrooms() != null) {
                    existingUnit.setBedrooms(unitUpdates.getBedrooms());
                }
                if (unitUpdates.getBathrooms() != null) {
                    existingUnit.setBathrooms(unitUpdates.getBathrooms());
                }
                if (unitUpdates.getSqft() != null) {
                    existingUnit.setSqft(unitUpdates.getSqft());
                }
                if (unitUpdates.getPrice() != null) {
                    existingUnit.setPrice(unitUpdates.getPrice());
                }
                if (unitUpdates.getStatus() != null) {
                    existingUnit.setStatus(unitUpdates.getStatus());
                }
                if (unitUpdates.getDescription() != null) {
                    existingUnit.setDescription(unitUpdates.getDescription());
                }
                if (unitUpdates.getFeatures() != null) {
                    existingUnit.setFeatures(unitUpdates.getFeatures());
                }
                if (unitUpdates.getImages() != null) {
                    existingUnit.setImages(unitUpdates.getImages());
                }
                if (unitUpdates.getCurrentStage() != null) {
                    existingUnit.setCurrentStage(unitUpdates.getCurrentStage());
                }
                if (unitUpdates.getBuyer() != null) {
                    existingUnit.setBuyer(unitUpdates.getBuyer());
                }

                Unit updatedUnit = unitRepository.save(existingUnit);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update unit status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Unit> updateUnitStatus(@PathVariable("id") Long id, @RequestParam("status") UnitStatus status) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setStatus(status);
                Unit updatedUnit = unitRepository.save(unit);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update construction stage
    @PatchMapping("/{id}/construction-stage")
    public ResponseEntity<Unit> updateConstructionStage(@PathVariable("id") Long id, @RequestParam("stage") ConstructionStage stage) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setCurrentStage(stage);
                Unit updatedUnit = unitRepository.save(unit);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Assign buyer to unit
    @PatchMapping("/{id}/assign-buyer/{buyerId}")
    public ResponseEntity<Unit> assignBuyerToUnit(@PathVariable("id") Long id, @PathVariable("buyerId") Long buyerId) {
        try {
            Unit updatedUnit = unitService.assignBuyerToUnit(id, buyerId);
            return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Remove buyer from unit
    @PatchMapping("/{id}/remove-buyer")
    public ResponseEntity<Unit> removeBuyerFromUnit(@PathVariable("id") Long id) {
        try {
            Unit updatedUnit = unitService.removeBuyerFromUnit(id);
            return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Toggle featured status
    @PatchMapping("/{id}/toggle-featured")
    public ResponseEntity<Unit> toggleFeaturedStatus(@PathVariable("id") Long id) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setIsFeatured(!unit.isFeatured());
                Unit updatedUnit = unitRepository.save(unit);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Add feature to unit
    @PatchMapping("/{id}/add-feature")
    public ResponseEntity<Unit> addFeatureToUnit(@PathVariable("id") Long id, @RequestParam("feature") String feature) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getFeatures() != null) {
                    unit.getFeatures().add(feature);
                }
                Unit updatedUnit = unitRepository.save(unit);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Remove feature from unit
    @PatchMapping("/{id}/remove-feature")
    public ResponseEntity<Unit> removeFeatureFromUnit(@PathVariable("id") Long id, @RequestParam("feature") String feature) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getFeatures() != null) {
                    unit.getFeatures().remove(feature);
                }
                Unit updatedUnit = unitRepository.save(unit);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
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
    public ResponseEntity<List<Unit>> getAvailableUnits() {
        try {
            List<Unit> units = unitRepository.findByStatus(UnitStatus.AVAILABLE);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get sold units
    @GetMapping("/sold")
    public ResponseEntity<List<Unit>> getSoldUnits() {
        try {
            List<Unit> units = unitRepository.findByStatus(UnitStatus.SOLD);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get reserved units
    @GetMapping("/reserved")
    public ResponseEntity<List<Unit>> getReservedUnits() {
        try {
            List<Unit> units = unitRepository.findByStatus(UnitStatus.RESERVED);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Search units by multiple criteria
    @GetMapping("/search")
    public ResponseEntity<List<Unit>> searchUnits(
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
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // GET - Get unit statistics for a project

    // @GetMapping("/project/{projectId}/statistics")
    // public ResponseEntity<UnitStatisticsDTO> getUnitStatisticsByProject(@PathVariable Long projectId) {
    //     try {
    //         UnitStatisticsDTO stats = UnitStatisticsDTO.builder()
    //                 .totalUnits(unitRepository.findByProjectId(projectId).size())
    //                 .availableUnits(unitService.getAvailableUnitsCountByProject(projectId))
    //                 .soldUnits(unitService.getSoldUnitsCountByProject(projectId))
    //                 .reservedUnits(unitService.getReservedUnitsCountByProject(projectId))
    //                 .averagePrice(unitService.getAveragePriceByProject(projectId))
    //                 .build();
    //         return ResponseEntity.ok(stats);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }
// GET - Get units by construction stage
    @GetMapping("/construction-stage/{stage}")
    public ResponseEntity<List<Unit>> getUnitsByConstructionStage(@PathVariable ConstructionStage stage) {
        try {
            List<Unit> units = unitRepository.findByCurrentStage(stage);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// PATCH - Bulk update unit status
    @PatchMapping("/bulk-update-status")
    public ResponseEntity<List<Unit>> bulkUpdateUnitStatus(
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

            return ResponseEntity.ok(updatedUnits);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

// GET - Get similar units (same type, similar price range)
    @GetMapping("/{id}/similar")
    public ResponseEntity<List<Unit>> getSimilarUnits(@PathVariable Long id) {
        try {
            Optional<Unit> unitOpt = unitRepository.findById(id);
            if (unitOpt.isPresent()) {
                Unit unit = unitOpt.get();
                BigDecimal priceRange = unit.getPrice().multiply(BigDecimal.valueOf(0.2)); // 20% range
                BigDecimal minPrice = unit.getPrice().subtract(priceRange);
                BigDecimal maxPrice = unit.getPrice().add(priceRange);

                List<Unit> similarUnits = unitRepository.findAll().stream()
                        .filter(u -> !u.getId().equals(id))
                        .filter(u -> u.getType() == unit.getType())
                        .filter(u -> u.getPrice().compareTo(minPrice) >= 0 && u.getPrice().compareTo(maxPrice) <= 0)
                        .limit(5)
                        .collect(Collectors.toList());

                return ResponseEntity.ok(similarUnits);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
