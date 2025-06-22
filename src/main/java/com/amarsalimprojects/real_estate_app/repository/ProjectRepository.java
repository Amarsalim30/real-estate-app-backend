package com.amarsalimprojects.real_estate_app.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.amarsalimprojects.real_estate_app.enums.ProjectStatus;
import com.amarsalimprojects.real_estate_app.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Optional<Project> findByName(String name);

    List<Project> findByStatus(ProjectStatus status);

    List<Project> findByCountyIgnoreCase(String county);

    List<Project> findBySubCountyIgnoreCase(String subCounty);

    List<Project> findByDeveloperNameIgnoreCase(String developerName);

    List<Project> findByMinPriceGreaterThanEqualAndMaxPriceLessThanEqual(BigDecimal minPrice, BigDecimal maxPrice);

    List<Project> findByConstructionProgressBetween(float minProgress, float maxProgress);

    List<Project> findByStartDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Project> findByTargetCompletionDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT p FROM Project p WHERE p.targetCompletionDate < :currentDate AND p.status != 'COMPLETED'")
    List<Project> findOverdueProjects(@Param("currentDate") LocalDateTime currentDate);

    @Query("SELECT COUNT(p) FROM Project p WHERE p.status = :status")
    Long countByStatus(@Param("status") ProjectStatus status);

    @Query("SELECT DISTINCT p.county FROM Project p WHERE p.county IS NOT NULL")
    List<String> findAllCounties();

    @Query("SELECT DISTINCT p.subCounty FROM Project p WHERE p.subCounty IS NOT NULL")
    List<String> findAllSubCounties();

    @Query("SELECT DISTINCT p.developerName FROM Project p WHERE p.developerName IS NOT NULL")
    List<String> findAllDevelopers();
}
