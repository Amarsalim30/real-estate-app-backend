package com.amarsalimprojects.real_estate_app.repository;

import com.amarsalimprojects.real_estate_app.model.Project;
import com.amarsalimprojects.real_estate_app.model.enums.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String name);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByLocation(String location);

    List<Project> findByCity(String city);

    List<Project> findByState(String state);

    List<Project> findByZipCode(String zipCode);

    List<Project> findByDeveloper(String developer);

    List<Project> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<Project> findByExpectedCompletionBetween(LocalDate startDate, LocalDate endDate);

    List<Project> findByConstructionProgressBetween(Integer minProgress, Integer maxProgress);

    List<Project> findByTotalUnitsBetween(Integer minUnits, Integer maxUnits);

    List<Project> findByAvailableUnitsGreaterThan(Integer units);

    List<Project> findByAvailableUnitsLessThan(Integer units);

    List<Project> findBySoldUnitsGreaterThan(Integer units);

    List<Project> findByReservedUnitsGreaterThan(Integer units);

    List<Project> findByStatusAndCity(ProjectStatus status, String city);

    List<Project> findByStatusAndState(ProjectStatus status, String state);

    List<Project> findByDeveloperAndStatus(String developer, ProjectStatus status);

    @Query("SELECT p FROM Project p WHERE p.priceRange.min >= :minPrice AND p.priceRange.max <= :maxPrice")
    List<Project> findByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT p FROM Project p WHERE p.priceRange.min >= :minPrice")
    List<Project> findByMinPriceGreaterThanEqual(@Param("minPrice") BigDecimal minPrice);

    @Query("SELECT p FROM Project p WHERE p.priceRange.max <= :maxPrice")
    List<Project> findByMaxPriceLessThanEqual(@Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT p FROM Project p WHERE p.name LIKE %:keyword% OR p.description LIKE %:keyword% OR p.location LIKE %:keyword%")
    List<Project> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Project p WHERE p.expectedCompletion < CURRENT_DATE AND p.status != 'COMPLETED'")
    List<Project> findOverdueProjects();

    @Query("SELECT p FROM Project p WHERE p.expectedCompletion BETWEEN CURRENT_DATE AND :date")
    List<Project> findProjectsCompletingBefore(@Param("date") LocalDate date);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    Long countByStatus(@Param("status") ProjectStatus status);

    @Query("SELECT SUM(p.totalUnits) FROM Project p WHERE p.status = :status")
    Integer getTotalUnitsByStatus(@Param("status") ProjectStatus status);

    @Query("SELECT SUM(p.availableUnits) FROM Project p")
    Integer getTotalAvailableUnits();

    @Query("SELECT SUM(p.soldUnits) FROM Project p")
    Integer getTotalSoldUnits();

    @Query("SELECT AVG(p.constructionProgress) FROM Project p WHERE p.status = 'UNDER_CONSTRUCTION'")
    Double getAverageConstructionProgress();

    @Query("SELECT p FROM Project p WHERE :amenity MEMBER OF p.amenities")
    List<Project> findByAmenity(@Param("amenity") String amenity);

    @Query("SELECT DISTINCT p.city FROM Project p ORDER BY p.city")
    List<String> findAllCities();

    @Query("SELECT DISTINCT p.state FROM Project p ORDER BY p.state")
    List<String> findAllStates();

    @Query("SELECT DISTINCT p.developer FROM Project p ORDER BY p.developer")
    List<String> findAllDevelopers();
}
