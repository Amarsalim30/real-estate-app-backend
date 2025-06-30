package com.amarsalimprojects.real_estate_app.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amarsalimprojects.real_estate_app.enums.ConstructionStage;
import com.amarsalimprojects.real_estate_app.enums.UnitStatus;
import com.amarsalimprojects.real_estate_app.enums.UnitType;
import com.amarsalimprojects.real_estate_app.model.Unit;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    Optional<Unit> findByUnitNumber(String unitNumber);

    List<Unit> findByProjectId(Long projectId);

    List<Unit> findByStatus(UnitStatus status);

    List<Unit> findByUnitType(UnitType unitType);

    List<Unit> findByFloor(Integer floor);

    List<Unit> findByBedrooms(Integer bedrooms);

    List<Unit> findByBathrooms(Integer bathrooms);

    List<Unit> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<Unit> findByBuyerId(Long buyerId);

    // Fix: Change from findByIsFeaturedTrue to findByFeaturedTrue
    List<Unit> findByFeaturedTrue();

    List<Unit> findByFeaturedFalse();

    List<Unit> findByCurrentStage(ConstructionStage stage);

    @Query("SELECT u FROM Unit u WHERE u.project.id = :projectId AND u.status = :status")
    List<Unit> findByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") UnitStatus status);

    @Query("SELECT u FROM Unit u WHERE u.bedrooms >= :minBedrooms AND u.bedrooms <= :maxBedrooms")
    List<Unit> findByBedroomsBetween(@Param("minBedrooms") Integer minBedrooms, @Param("maxBedrooms") Integer maxBedrooms);

    @Query("SELECT u FROM Unit u WHERE u.sqft >= :minSqft AND u.sqft <= :maxSqft")
    List<Unit> findBySqftBetween(@Param("minSqft") Integer minSqft, @Param("maxSqft") Integer maxSqft);

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.project.id = :projectId AND u.status = :status")
    Long countByProjectIdAndStatus(@Param("projectId") Long projectId, @Param("status") UnitStatus status);

    @Query("SELECT u FROM Unit u WHERE u.buyer IS NULL AND u.status = 'AVAILABLE'")
    List<Unit> findAvailableUnitsWithoutBuyer();

    @Query("SELECT u FROM Unit u WHERE u.buyer IS NOT NULL")
    List<Unit> findUnitsWithBuyer();

    // Additional useful queries
    @Query("SELECT u FROM Unit u WHERE u.project.id = :projectId AND u.unitType = :unitType")
    List<Unit> findByProjectIdAndUnitType(@Param("projectId") Long projectId, @Param("unitType") UnitType unitType);

    @Query("SELECT COUNT(u) FROM Unit u WHERE u.project.id = :projectId")
    Long countByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT u FROM Unit u WHERE u.price <= :maxPrice AND u.status = 'AVAILABLE'")
    List<Unit> findAvailableUnitsUnderPrice(@Param("maxPrice") BigDecimal maxPrice);

    boolean existsByUnitNumber(String unitNumber);
}
