package com.amarsalimprojects.real_estate_app.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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
import com.amarsalimprojects.real_estate_app.model.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.enums.UnitType;
import com.amarsalimprojects.real_estate_app.repository.UnitRepository;
import com.amarsalimprojects.real_estate_app.requests.UnitRequest;
import com.amarsalimprojects.real_estate_app.requests.UnitStatistics;
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
    public ResponseEntity<Unit> createUnit(@RequestBody UnitRequest request) {
        try {
            // Check if unit number already exists
            if (request.getUnitNumber() != null && !request.getUnitNumber().isEmpty()) {
                Optional<Unit> existingUnit = unitRepository.findByUnitNumber(request.getUnitNumber());
                if (existingUnit.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }

            Unit savedUnit = unitService.createUnit(request);
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

    // READ - Get units by bathrooms
    @GetMapping("/bathrooms/{bathrooms}")
    public ResponseEntity<List<Unit>> getUnitsByBathrooms(@PathVariable("bathrooms") Integer bathrooms) {
        try {
            List<Unit> units = unitRepository.findByBathrooms(bathrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by bedrooms and bathrooms
    @GetMapping("/bedrooms/{bedrooms}/bathrooms/{bathrooms}")
    public ResponseEntity<List<Unit>> getUnitsByBedroomsAndBathrooms(
            @PathVariable("bedrooms") Integer bedrooms,
            @PathVariable("bathrooms") Integer bathrooms) {
        try {
            List<Unit> units = unitRepository.findByBedroomsAndBathrooms(bedrooms, bathrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by sqft range
    @GetMapping("/sqft-range")
    public ResponseEntity<List<Unit>> getUnitsBySqftRange(
            @RequestParam("minSqft") Integer minSqft,
            @RequestParam("maxSqft") Integer maxSqft) {
        try {
            List<Unit> units = unitRepository.findBySqftBetween(minSqft, maxSqft);
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

    // READ - Get units by minimum price
    @GetMapping("/min-price/{minPrice}")
    public ResponseEntity<List<Unit>> getUnitsByMinPrice(@PathVariable("minPrice") BigDecimal minPrice) {
        try {
            List<Unit> units = unitRepository.findByPriceGreaterThanEqual(minPrice);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by maximum price
    @GetMapping("/max-price/{maxPrice}")
    public ResponseEntity<List<Unit>> getUnitsByMaxPrice(@PathVariable("maxPrice") BigDecimal maxPrice) {
        try {
            List<Unit> units = unitRepository.findByPriceLessThanEqual(maxPrice);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by project and status
    @GetMapping("/project/{projectId}/status/{status}")
    public ResponseEntity<List<Unit>> getUnitsByProjectAndStatus(
            @PathVariable("projectId") Long projectId,
            @PathVariable("status") UnitStatus status) {
        try {
            List<Unit> units = unitRepository.findByProjectIdAndStatus(projectId, status);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by project and type
    @GetMapping("/project/{projectId}/type/{type}")
    public ResponseEntity<List<Unit>> getUnitsByProjectAndType(
            @PathVariable("projectId") Long projectId,
            @PathVariable("type") UnitType type) {
        try {
            List<Unit> units = unitRepository.findByProjectIdAndType(projectId, type);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by project and floor
    @GetMapping("/project/{projectId}/floor/{floor}")
    public ResponseEntity<List<Unit>> getUnitsByProjectAndFloor(
            @PathVariable("projectId") Long projectId,
            @PathVariable("floor") Integer floor) {
        try {
            List<Unit> units = unitRepository.findByProjectIdAndFloor(projectId, floor);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by status and type
    @GetMapping("/status/{status}/type/{type}")
    public ResponseEntity<List<Unit>> getUnitsByStatusAndType(
            @PathVariable("status") UnitStatus status,
            @PathVariable("type") UnitType type) {
        try {
            List<Unit> units = unitRepository.findByStatusAndType(status, type);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by status and bedrooms
    @GetMapping("/status/{status}/bedrooms/{bedrooms}")
    public ResponseEntity<List<Unit>> getUnitsByStatusAndBedrooms(
            @PathVariable("status") UnitStatus status,
            @PathVariable("bedrooms") Integer bedrooms) {
        try {
            List<Unit> units = unitRepository.findByStatusAndBedrooms(status, bedrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by type and bedrooms
    @GetMapping("/type/{type}/bedrooms/{bedrooms}")
    public ResponseEntity<List<Unit>> getUnitsByTypeAndBedrooms(
            @PathVariable("type") UnitType type,
            @PathVariable("bedrooms") Integer bedrooms) {
        try {
            List<Unit> units = unitRepository.findByTypeAndBedrooms(type, bedrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by type, bedrooms and bathrooms
    @GetMapping("/type/{type}/bedrooms/{bedrooms}/bathrooms/{bathrooms}")
    public ResponseEntity<List<Unit>> getUnitsByTypeBedroomsAndBathrooms(
            @PathVariable("type") UnitType type,
            @PathVariable("bedrooms") Integer bedrooms,
            @PathVariable("bathrooms") Integer bathrooms) {
        try {
            List<Unit> units = unitRepository.findByTypeAndBedroomsAndBathrooms(type, bedrooms, bathrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by floor range
    @GetMapping("/floor-range")
    public ResponseEntity<List<Unit>> getUnitsByFloorRange(
            @RequestParam("minFloor") Integer minFloor,
            @RequestParam("maxFloor") Integer maxFloor) {
        try {
            List<Unit> units = unitRepository.findByFloorBetween(minFloor, maxFloor);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by bedrooms range
    @GetMapping("/bedrooms-range")
    public ResponseEntity<List<Unit>> getUnitsByBedroomsRange(
            @RequestParam("minBedrooms") Integer minBedrooms,
            @RequestParam("maxBedrooms") Integer maxBedrooms) {
        try {
            List<Unit> units = unitRepository.findByBedroomsBetween(minBedrooms, maxBedrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by bathrooms range
    @GetMapping("/bathrooms-range")
    public ResponseEntity<List<Unit>> getUnitsByBathroomsRange(
            @RequestParam("minBathrooms") Integer minBathrooms,
            @RequestParam("maxBathrooms") Integer maxBathrooms) {
        try {
            List<Unit> units = unitRepository.findByBathroomsBetween(minBathrooms, maxBathrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units reserved by buyer
    @GetMapping("/reserved-by/{buyerId}")
    public ResponseEntity<List<Unit>> getUnitsReservedByBuyer(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Unit> units = unitRepository.findByReservedById(buyerId);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units sold to buyer
    @GetMapping("/sold-to/{buyerId}")
    public ResponseEntity<List<Unit>> getUnitsSoldToBuyer(@PathVariable("buyerId") Long buyerId) {
        try {
            List<Unit> units = unitRepository.findBySoldToId(buyerId);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units reserved within date range
    @GetMapping("/reserved-date-range")
    public ResponseEntity<List<Unit>> getUnitsReservedInDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Unit> units = unitRepository.findByReservedDateBetween(startDate, endDate);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units sold within date range
    @GetMapping("/sold-date-range")
    public ResponseEntity<List<Unit>> getUnitsSoldInDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Unit> units = unitRepository.findBySoldDateBetween(startDate, endDate);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units reserved after date
    @GetMapping("/reserved-after/{date}")
    public ResponseEntity<List<Unit>> getUnitsReservedAfter(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Unit> units = unitRepository.findByReservedDateAfter(date);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units sold after date
    @GetMapping("/sold-after/{date}")
    public ResponseEntity<List<Unit>> getUnitsSoldAfter(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Unit> units = unitRepository.findBySoldDateAfter(date);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units reserved before date
    @GetMapping("/reserved-before/{date}")
    public ResponseEntity<List<Unit>> getUnitsReservedBefore(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Unit> units = unitRepository.findByReservedDateBefore(date);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units sold before date
    @GetMapping("/sold-before/{date}")
    public ResponseEntity<List<Unit>> getUnitsSoldBefore(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Unit> units = unitRepository.findBySoldDateBefore(date);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Search units by unit number containing
    @GetMapping("/search/unit-number/{unitNumber}")
    public ResponseEntity<List<Unit>> searchUnitsByUnitNumber(@PathVariable("unitNumber") String unitNumber) {
        try {
            List<Unit> units = unitRepository.findByUnitNumberContaining(unitNumber);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Search units by description containing keyword
    @GetMapping("/search/description/{keyword}")
    public ResponseEntity<List<Unit>> searchUnitsByDescription(@PathVariable("keyword") String keyword) {
        try {
            List<Unit> units = unitRepository.findByDescriptionContaining(keyword);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units by feature
    @GetMapping("/feature/{feature}")
    public ResponseEntity<List<Unit>> getUnitsByFeature(@PathVariable("feature") String feature) {
        try {
            List<Unit> units = unitRepository.findByFeature(feature);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units with features
    @GetMapping("/with-features")
    public ResponseEntity<List<Unit>> getUnitsWithFeatures() {
        try {
            List<Unit> units = unitRepository.findUnitsWithFeatures();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units with images
    @GetMapping("/with-images")
    public ResponseEntity<List<Unit>> getUnitsWithImages() {
        try {
            List<Unit> units = unitRepository.findUnitsWithImages();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units with minimum features
    @GetMapping("/min-features/{minFeatures}")
    public ResponseEntity<List<Unit>> getUnitsWithMinimumFeatures(@PathVariable("minFeatures") int minFeatures) {
        try {
            List<Unit> units = unitRepository.findUnitsWithMinimumFeatures(minFeatures);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get units with minimum images
    @GetMapping("/min-images/{minImages}")
    public ResponseEntity<List<Unit>> getUnitsWithMinimumImages(@PathVariable("minImages") int minImages) {
        try {
            List<Unit> units = unitRepository.findUnitsWithMinimumImages(minImages);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get available units sorted by price (ascending)
    @GetMapping("/available/price-asc")
    public ResponseEntity<List<Unit>> getAvailableUnitsByPriceAsc() {
        try {
            List<Unit> units = unitRepository.findAvailableUnitsByPriceAsc();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get available units sorted by price (descending)
    @GetMapping("/available/price-desc")
    public ResponseEntity<List<Unit>> getAvailableUnitsByPriceDesc() {
        try {
            List<Unit> units = unitRepository.findAvailableUnitsByPriceDesc();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get available units sorted by sqft (ascending)
    @GetMapping("/available/sqft-asc")
    public ResponseEntity<List<Unit>> getAvailableUnitsBySqftAsc() {
        try {
            List<Unit> units = unitRepository.findAvailableUnitsBySqftAsc();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get available units sorted by sqft (descending)
    @GetMapping("/available/sqft-desc")
    public ResponseEntity<List<Unit>> getAvailableUnitsBySqftDesc() {
        try {
            List<Unit> units = unitRepository.findAvailableUnitsBySqftDesc();
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get expired reservations
    @GetMapping("/expired-reservations/{date}")
    public ResponseEntity<List<Unit>> getExpiredReservations(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Unit> units = unitRepository.findExpiredReservations(date);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get recent reservations
    @GetMapping("/recent-reservations/{date}")
    public ResponseEntity<List<Unit>> getRecentReservations(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Unit> units = unitRepository.findRecentReservations(date);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get recent sales
    @GetMapping("/recent-sales/{date}")
    public ResponseEntity<List<Unit>> getRecentSales(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Unit> units = unitRepository.findRecentSales(date);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get floors by project
    @GetMapping("/project/{projectId}/floors")
    public ResponseEntity<List<Integer>> getFloorsByProject(@PathVariable("projectId") Long projectId) {
        try {
            List<Integer> floors = unitRepository.findFloorsByProjectId(projectId);
            if (floors.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(floors, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get unit types by project
    @GetMapping("/project/{projectId}/unit-types")
    public ResponseEntity<List<UnitType>> getUnitTypesByProject(@PathVariable("projectId") Long projectId) {
        try {
            List<UnitType> unitTypes = unitRepository.findUnitTypesByProjectId(projectId);
            if (unitTypes.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(unitTypes, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get available units by project and floor
    @GetMapping("/available/project/{projectId}/floor/{floor}")
    public ResponseEntity<List<Unit>> getAvailableUnitsByProjectAndFloor(
            @PathVariable("projectId") Long projectId,
            @PathVariable("floor") Integer floor) {
        try {
            List<Unit> units = unitRepository.findAvailableUnitsByProjectAndFloor(projectId, floor);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get available units by price range
    @GetMapping("/available/price-range")
    public ResponseEntity<List<Unit>> getAvailableUnitsByPriceRange(
            @RequestParam("minPrice") BigDecimal minPrice,
            @RequestParam("maxPrice") BigDecimal maxPrice) {
        try {
            List<Unit> units = unitRepository.findAvailableUnitsByPriceRange(minPrice, maxPrice);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get available units by sqft range
    @GetMapping("/available/sqft-range")
    public ResponseEntity<List<Unit>> getAvailableUnitsBySqftRange(
            @RequestParam("minSqft") Integer minSqft,
            @RequestParam("maxSqft") Integer maxSqft) {
        try {
            List<Unit> units = unitRepository.findAvailableUnitsBySqftRange(minSqft, maxSqft);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get available units by bedrooms and bathrooms
    @GetMapping("/available/bedrooms/{bedrooms}/bathrooms/{bathrooms}")
    public ResponseEntity<List<Unit>> getAvailableUnitsByBedroomsAndBathrooms(
            @PathVariable("bedrooms") Integer bedrooms,
            @PathVariable("bathrooms") Integer bathrooms) {
        try {
            List<Unit> units = unitRepository.findAvailableUnitsByBedroomsAndBathrooms(bedrooms, bathrooms);
            if (units.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(units, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Full update of unit
    @PutMapping("/{id}")
    public ResponseEntity<Unit> updateUnit(@PathVariable("id") Long id, @RequestBody Unit unit) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                unit.setId(id);
                Unit updatedUnit = unitRepository.save(unit);
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
                if (unitUpdates.getReservedBy() != null) {
                    existingUnit.setReservedBy(unitUpdates.getReservedBy());
                }
                if (unitUpdates.getReservedDate() != null) {
                    existingUnit.setReservedDate(unitUpdates.getReservedDate());
                }
                if (unitUpdates.getSoldTo() != null) {
                    existingUnit.setSoldTo(unitUpdates.getSoldTo());
                }
                if (unitUpdates.getSoldDate() != null) {
                    existingUnit.setSoldDate(unitUpdates.getSoldDate());
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

    // UPDATE - Update unit price
    @PatchMapping("/{id}/price")
    public ResponseEntity<Unit> updateUnitPrice(@PathVariable("id") Long id, @RequestParam("price") BigDecimal price) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setPrice(price);
                Unit updatedUnit = unitRepository.save(unit);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update unit description
    @PatchMapping("/{id}/description")
    public ResponseEntity<Unit> updateUnitDescription(@PathVariable("id") Long id, @RequestParam("description") String description) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                unit.setDescription(description);
                Unit updatedUnit = unitRepository.save(unit);
                return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Reserve unit
    @PatchMapping("/{id}/reserve")
    public ResponseEntity<Unit> reserveUnit(@PathVariable("id") Long id, @RequestParam("buyerId") Long buyerId) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getStatus() == UnitStatus.AVAILABLE) {
                    unit.setStatus(UnitStatus.RESERVED);
                    unit.setReservedBy(null); // Set buyer entity if needed
                    unit.setReservedDate(LocalDate.now());
                    Unit updatedUnit = unitRepository.save(unit);
                    return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Sell unit
    @PatchMapping("/{id}/sell")
    public ResponseEntity<Unit> sellUnit(@PathVariable("id") Long id, @RequestParam("buyerId") Long buyerId) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getStatus() == UnitStatus.AVAILABLE || unit.getStatus() == UnitStatus.RESERVED) {
                    unit.setStatus(UnitStatus.SOLD);
                    unit.setSoldTo(null); // Set buyer entity if needed
                    unit.setSoldDate(LocalDate.now());
                    Unit updatedUnit = unitRepository.save(unit);
                    return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Cancel reservation
    @PatchMapping("/{id}/cancel-reservation")
    public ResponseEntity<Unit> cancelReservation(@PathVariable("id") Long id) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getStatus() == UnitStatus.RESERVED) {
                    unit.setStatus(UnitStatus.AVAILABLE);
                    unit.setReservedBy(null);
                    unit.setReservedDate(null);
                    Unit updatedUnit = unitRepository.save(unit);
                    return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
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
                if (unit.getFeatures() == null) {
                    unit.setFeatures(new java.util.ArrayList<>());
                }
                if (!unit.getFeatures().contains(feature)) {
                    unit.getFeatures().add(feature);
                    Unit updatedUnit = unitRepository.save(unit);
                    return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(unit, HttpStatus.OK);
                }
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
                if (unit.getFeatures() != null && unit.getFeatures().contains(feature)) {
                    unit.getFeatures().remove(feature);
                    Unit updatedUnit = unitRepository.save(unit);
                    return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(unit, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Add image to unit
    @PatchMapping("/{id}/add-image")
    public ResponseEntity<Unit> addImageToUnit(@PathVariable("id") Long id, @RequestParam("imageUrl") String imageUrl) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getImages() == null) {
                    unit.setImages(new java.util.ArrayList<>());
                }
                if (!unit.getImages().contains(imageUrl)) {
                    unit.getImages().add(imageUrl);
                    Unit updatedUnit = unitRepository.save(unit);
                    return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(unit, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Remove image from unit
    @PatchMapping("/{id}/remove-image")
    public ResponseEntity<Unit> removeImageFromUnit(@PathVariable("id") Long id, @RequestParam("imageUrl") String imageUrl) {
        try {
            Optional<Unit> existingUnit = unitRepository.findById(id);
            if (existingUnit.isPresent()) {
                Unit unit = existingUnit.get();
                if (unit.getImages() != null && unit.getImages().contains(imageUrl)) {
                    unit.getImages().remove(imageUrl);
                    Unit updatedUnit = unitRepository.save(unit);
                    return new ResponseEntity<>(updatedUnit, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(unit, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Bulk update unit status
    @PatchMapping("/bulk-update-status")
    public ResponseEntity<List<Unit>> bulkUpdateUnitStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("status") UnitStatus status) {
        try {
            List<Unit> updatedUnits = ids.stream()
                    .map(id -> unitRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(unit -> unit.setStatus(status))
                    .map(unit -> unitRepository.save(unit))
                    .toList();

            if (updatedUnits.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUnits, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Bulk update unit prices
    @PatchMapping("/bulk-update-price")
    public ResponseEntity<List<Unit>> bulkUpdateUnitPrice(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("price") BigDecimal price) {
        try {
            List<Unit> updatedUnits = ids.stream()
                    .map(id -> unitRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(unit -> unit.setPrice(price))
                    .map(unit -> unitRepository.save(unit))
                    .toList();

            if (updatedUnits.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUnits, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Bulk price adjustment (percentage)
    @PatchMapping("/bulk-adjust-price")
    public ResponseEntity<List<Unit>> bulkAdjustUnitPrice(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("percentage") Double percentage) {
        try {
            List<Unit> updatedUnits = ids.stream()
                    .map(id -> unitRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(unit -> {
                        if (unit.getPrice() != null) {
                            BigDecimal adjustment = unit.getPrice().multiply(BigDecimal.valueOf(percentage / 100));
                            unit.setPrice(unit.getPrice().add(adjustment));
                        }
                    })
                    .map(unit -> unitRepository.save(unit))
                    .toList();

            if (updatedUnits.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedUnits, HttpStatus.OK);
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

    // DELETE - Delete all units
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllUnits() {
        try {
            unitRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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

    // DELETE - Delete units by status
    @DeleteMapping("/status/{status}")
    public ResponseEntity<HttpStatus> deleteUnitsByStatus(@PathVariable("status") UnitStatus status) {
        try {
            List<Unit> units = unitRepository.findByStatus(status);
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

    // DELETE - Delete units by type
    @DeleteMapping("/type/{type}")
    public ResponseEntity<HttpStatus> deleteUnitsByType(@PathVariable("type") UnitType type) {
        try {
            List<Unit> units = unitRepository.findByType(type);
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

    // DELETE - Delete units by floor
    @DeleteMapping("/floor/{floor}")
    public ResponseEntity<HttpStatus> deleteUnitsByFloor(@PathVariable("floor") Integer floor) {
        try {
            List<Unit> units = unitRepository.findByFloor(floor);
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

    // DELETE - Bulk delete units
    @DeleteMapping("/bulk-delete")
    public ResponseEntity<HttpStatus> bulkDeleteUnits(@RequestParam("ids") List<Long> ids) {
        try {
            List<Unit> unitsToDelete = ids.stream()
                    .map(id -> unitRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            if (!unitsToDelete.isEmpty()) {
                unitRepository.deleteAll(unitsToDelete);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get unit count
    @GetMapping("/count")
    public ResponseEntity<Long> getUnitCount() {
        try {
            long count = unitRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get unit count by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getUnitCountByStatus(@PathVariable("status") UnitStatus status) {
        try {
            Long count = unitRepository.countByStatus(status);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get unit count by type
    @GetMapping("/count/type/{type}")
    public ResponseEntity<Long> getUnitCountByType(@PathVariable("type") UnitType type) {
        try {
            Long count = unitRepository.countByType(type);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get unit count by project
    @GetMapping("/count/project/{projectId}")
    public ResponseEntity<Long> getUnitCountByProject(@PathVariable("projectId") Long projectId) {
        try {
            Long count = unitRepository.countByProjectId(projectId);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get unit count by project and status
    @GetMapping("/count/project/{projectId}/status/{status}")
    public ResponseEntity<Long> getUnitCountByProjectAndStatus(
            @PathVariable("projectId") Long projectId,
            @PathVariable("status") UnitStatus status) {
        try {
            Long count = unitRepository.countByProjectIdAndStatus(projectId, status);
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get average price by status
    @GetMapping("/average-price/status/{status}")
    public ResponseEntity<BigDecimal> getAveragePriceByStatus(@PathVariable("status") UnitStatus status) {
        try {
            BigDecimal averagePrice = unitRepository.getAveragePriceByStatus(status);
            return new ResponseEntity<>(averagePrice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get average price by type
    @GetMapping("/average-price/type/{type}")
    public ResponseEntity<BigDecimal> getAveragePriceByType(@PathVariable("type") UnitType type) {
        try {
            BigDecimal averagePrice = unitRepository.getAveragePriceByType(type);
            return new ResponseEntity<>(averagePrice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get average sqft by type
    @GetMapping("/average-sqft/type/{type}")
    public ResponseEntity<Double> getAverageSqftByType(@PathVariable("type") UnitType type) {
        try {
            Double averageSqft = unitRepository.getAverageSqftByType(type);
            return new ResponseEntity<>(averageSqft, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get minimum available price
    @GetMapping("/min-available-price")
    public ResponseEntity<BigDecimal> getMinAvailablePrice() {
        try {
            BigDecimal minPrice = unitRepository.getMinAvailablePrice();
            return new ResponseEntity<>(minPrice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get maximum available price
    @GetMapping("/max-available-price")
    public ResponseEntity<BigDecimal> getMaxAvailablePrice() {
        try {
            BigDecimal maxPrice = unitRepository.getMaxAvailablePrice();
            return new ResponseEntity<>(maxPrice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // STATISTICS - Get comprehensive unit statistics
    @GetMapping("/statistics")
    public ResponseEntity<UnitStatistics> getUnitStatistics() {
        try {
            UnitStatistics stats = UnitStatistics.builder()
                    .totalUnits(unitRepository.count())
                    .availableUnits(unitRepository.countByStatus(UnitStatus.AVAILABLE))
                    .reservedUnits(unitRepository.countByStatus(UnitStatus.RESERVED))
                    .soldUnits(unitRepository.countByStatus(UnitStatus.SOLD))
                    .studioUnits(unitRepository.countByType(UnitType.STUDIO))
                    .oneBrUnits(unitRepository.countByType(UnitType.ONE_BR))
                    .twoBrUnits(unitRepository.countByType(UnitType.TWO_BR))
                    .threeBrUnits(unitRepository.countByType(UnitType.THREE_BR))
                    .fourBrUnits(unitRepository.countByType(UnitType.FOUR_BR))
                    .penthouseUnits(unitRepository.countByType(UnitType.PENTHOUSE))
                    .averageAvailablePrice(unitRepository.getAveragePriceByStatus(UnitStatus.AVAILABLE))
                    .minAvailablePrice(unitRepository.getMinAvailablePrice())
                    .maxAvailablePrice(unitRepository.getMaxAvailablePrice())
                    .build();

            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
