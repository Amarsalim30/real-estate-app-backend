package com.amarsalimprojects.real_estate_app.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amarsalimprojects.real_estate_app.model.Unit;
import com.amarsalimprojects.real_estate_app.model.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.model.enums.UnitType;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    Optional<Unit> findByUnitNumber(String unitNumber);

    List<Unit> findByProjectId(Long projectId);

    List<Unit> findByStatus(UnitStatus status);

    List<Unit> findByType(UnitType type);

    List<Unit> findByFloor(Integer floor);

    List<Unit> findByBedrooms(Integer bedrooms);

    List<Unit> findByBathrooms(Integer bathrooms);

    List<Unit> findByBedroomsAndBathrooms(Integer bedrooms, Integer bathrooms);

    List<Unit> findBySqftBetween(Integer minSqft, Integer maxSqft);

    List<Unit> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Unit> findByPriceGreaterThanEqual(BigDecimal minPrice);

    List<Unit> findByPriceLessThanEqual(BigDecimal maxPrice);

    List<Unit> findByProjectIdAndStatus(Long projectId, UnitStatus status);

    List<Unit> findByProjectIdAndType(Long projectId, UnitType type);

    List<Unit> findByProjectIdAndFloor(Long projectId, Integer floor);

    List<Unit> findByStatusAndType(UnitStatus status, UnitType type);

    List<Unit> findByStatusAndBedrooms(UnitStatus status, Integer bedrooms);

    List<Unit> findByTypeAndBedrooms(UnitType type, Integer bedrooms);

    List<Unit> findByTypeAndBedroomsAndBathrooms(UnitType type, Integer bedrooms, Integer bathrooms);

    List<Unit> findByFloorBetween(Integer minFloor, Integer maxFloor);

    List<Unit> findByBedroomsBetween(Integer minBedrooms, Integer maxBedrooms);

    List<Unit> findByBathroomsBetween(Integer minBathrooms, Integer maxBathrooms);

    List<Unit> findByReservedById(Long buyerId);

    List<Unit> findBySoldToId(Long buyerId);

    List<Unit> findByReservedDateBetween(LocalDate startDate, LocalDate endDate);

    List<Unit> findBySoldDateBetween(LocalDate startDate, LocalDate endDate);

    List<Unit> findByReservedDateAfter(LocalDate date);

    List<Unit> findBySoldDateAfter(LocalDate date);

    List<Unit> findByReservedDateBefore(LocalDate date);

    List<Unit> findBySoldDateBefore(LocalDate date);

    @Query("SELECT u FROM Unit u WHERE u.unitNumber LIKE %:unitNumber%")
    List<Unit> findByUnitNumberContaining(@Param("unitNumber") String unitNumber);

    @Query("SELECT u FROM Unit u WHERE u.description LIKE %:keyword%")
    List<Unit> findByDescriptionContaining(@Param("keyword") String keyword);

    @Query("SELECT u FROM Unit u WHERE :feature MEMBER OF u.features")
    List<Unit> findByFeature(@Param("feature") String feature);

    @Query("SELECT u FROM Unit u WHERE u.features IS NOT EMPTY")
    List<Unit> findUnitsWithFeatures();

    @Query("SELECT u FROM Unit u WHERE u.images IS NOT EMPTY")
    List<Unit> findUnitsWithImages();

    @Query("SELECT u FROM Unit u WHERE SIZE(u.features) >= :minFeatures")
    List<Unit> findUnitsWithMinimumFeatures(@Param("minFeatures") int minFeatures);

    @Query("SELECT u FROM Unit u WHERE SIZE(u.images) >= :minImages")
    List<Unit> findUnitsWithMinimumImages(@Param("minImages") int minImages);

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.status = :status")
    Long countByStatus(@Param("status") UnitStatus status);

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.type = :type")
    Long countByType(@Param("type") UnitType type);

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.project.id = :projectId")
    Long countByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.project.id = :projectId AND u.status = :status")
    Long countByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") UnitStatus status);

    @Query("SELECT AVG(u.price) FROM Unit u WHERE u.status = :status")
    BigDecimal getAveragePriceByStatus(@Param("status") UnitStatus status);

    @Query("SELECT AVG(u.price) FROM Unit u WHERE u.type = :type")
    BigDecimal getAveragePriceByType(@Param("type") UnitType type);

    @Query("SELECT AVG(u.sqft) FROM Unit u WHERE u.type = :type")
    Double getAverageSqftByType(@Param("type") UnitType type);

    @Query("SELECT MIN(u.price) FROM Unit u WHERE u.status = 'AVAILABLE'")
    BigDecimal getMinAvailablePrice();

    @Query("SELECT MAX(u.price) FROM Unit u WHERE u.status = 'AVAILABLE'")
    BigDecimal getMaxAvailablePrice();

    @Query("SELECT u FROM Unit u WHERE u.status = 'AVAILABLE' ORDER BY u.price ASC")
    List<Unit> findAvailableUnitsByPriceAsc();

    @Query("SELECT u FROM Unit u WHERE u.status = 'AVAILABLE' ORDER BY u.price DESC")
    List<Unit> findAvailableUnitsByPriceDesc();

    @Query("SELECT u FROM Unit u WHERE u.status = 'AVAILABLE' ORDER BY u.sqft ASC")
    List<Unit> findAvailableUnitsBySqftAsc();

    @Query("SELECT u FROM Unit u WHERE u.status = 'AVAILABLE' ORDER BY u.sqft DESC")
    List<Unit> findAvailableUnitsBySqftDesc();

    @Query("SELECT u FROM Unit u WHERE u.status = 'RESERVED' AND u.reservedDate < :date")
    List<Unit> findExpiredReservations(@Param("date") LocalDate date);

    @Query("SELECT u FROM Unit u WHERE u.status = 'RESERVED' AND u.reservedDate >= :date")
    List<Unit> findRecentReservations(@Param("date") LocalDate date);

    @Query("SELECT u FROM Unit u WHERE u.status = 'SOLD' AND u.soldDate >= :date")
    List<Unit> findRecentSales(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT u.floor FROM Unit u WHERE u.project.id = :projectId ORDER BY u.floor")
    List<Integer> findFloorsByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT DISTINCT u.type FROM Unit u WHERE u.project.id = :projectId")
    List<UnitType> findUnitTypesByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT u FROM Unit u WHERE u.project.id = :projectId AND u.floor = :floor AND u.status = 'AVAILABLE'")
    List<Unit> findAvailableUnitsByProjectAndFloor(@Param("projectId") Long projectId, @Param("floor") Integer floor);

    @Query("SELECT u FROM Unit u WHERE u.price BETWEEN :minPrice AND :maxPrice AND u.status = 'AVAILABLE'")
    List<Unit> findAvailableUnitsByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT u FROM Unit u WHERE u.sqft BETWEEN :minSqft AND :maxSqft AND u.status = 'AVAILABLE'")
    List<Unit> findAvailableUnitsBySqftRange(@Param("minSqft") Integer minSqft, @Param("maxSqft") Integer maxSqft);

    @Query("SELECT u FROM Unit u WHERE u.bedrooms = :bedrooms AND u.bathrooms = :bathrooms AND u.status = 'AVAILABLE'")
    List<Unit> findAvailableUnitsByBedroomsAndBathrooms(@Param("bedrooms") Integer bedrooms, @Param("bathrooms") Integer bathrooms);
}
