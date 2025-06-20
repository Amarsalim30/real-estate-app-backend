package com.amarsalimprojects.real_estate_app.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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

import com.amarsalimprojects.real_estate_app.dto.ProjectDTO;
import com.amarsalimprojects.real_estate_app.model.Project;
import com.amarsalimprojects.real_estate_app.model.enums.ProjectStatus;
import com.amarsalimprojects.real_estate_app.repository.ProjectRepository;
import com.amarsalimprojects.real_estate_app.service.ProjectService;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    private ProjectService projectService;

    // CREATE - Add a new project
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        try {
            // Check if project name already exists
            if (project.getName() != null && !project.getName().isEmpty()) {
                Optional<Project> existingProject = projectRepository.findByName(project.getName());
                if (existingProject.isPresent()) {
                    return new ResponseEntity<>(null, HttpStatus.CONFLICT);
                }
            }

            Project savedProject = projectRepository.save(project);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all projects
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects();
        return ResponseEntity.ok(projects);
    }

    // READ - Get project by ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable("id") Long id) {
        try {
            Optional<Project> project = projectRepository.findById(id);
            if (project.isPresent()) {
                return new ResponseEntity<>(project.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get project by name
    @GetMapping("/name/{name}")
    public ResponseEntity<Project> getProjectByName(@PathVariable("name") String name) {
        try {
            Optional<Project> project = projectRepository.findByName(name);
            if (project.isPresent()) {
                return new ResponseEntity<>(project.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Project>> getProjectsByStatus(@PathVariable("status") ProjectStatus status) {
        try {
            List<Project> projects = projectRepository.findByStatus(status);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by location
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Project>> getProjectsByLocation(@PathVariable("location") String location) {
        try {
            List<Project> projects = projectRepository.findByLocation(location);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by city
    @GetMapping("/city/{city}")
    public ResponseEntity<List<Project>> getProjectsByCity(@PathVariable("city") String city) {
        try {
            List<Project> projects = projectRepository.findByCity(city);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by state
    @GetMapping("/state/{state}")
    public ResponseEntity<List<Project>> getProjectsByState(@PathVariable("state") String state) {
        try {
            List<Project> projects = projectRepository.findByState(state);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by zip code
    @GetMapping("/zipcode/{zipCode}")
    public ResponseEntity<List<Project>> getProjectsByZipCode(@PathVariable("zipCode") String zipCode) {
        try {
            List<Project> projects = projectRepository.findByZipCode(zipCode);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by developer
    @GetMapping("/developer/{developer}")
    public ResponseEntity<List<Project>> getProjectsByDeveloper(@PathVariable("developer") String developer) {
        try {
            List<Project> projects = projectRepository.findByDeveloper(developer);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by status and city
    @GetMapping("/status/{status}/city/{city}")
    public ResponseEntity<List<Project>> getProjectsByStatusAndCity(
            @PathVariable("status") ProjectStatus status,
            @PathVariable("city") String city) {
        try {
            List<Project> projects = projectRepository.findByStatusAndCity(status, city);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by status and state
    @GetMapping("/status/{status}/state/{state}")
    public ResponseEntity<List<Project>> getProjectsByStatusAndState(
            @PathVariable("status") ProjectStatus status,
            @PathVariable("state") String state) {
        try {
            List<Project> projects = projectRepository.findByStatusAndState(status, state);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by developer and status
    @GetMapping("/developer/{developer}/status/{status}")
    public ResponseEntity<List<Project>> getProjectsByDeveloperAndStatus(
            @PathVariable("developer") String developer,
            @PathVariable("status") ProjectStatus status) {
        try {
            List<Project> projects = projectRepository.findByDeveloperAndStatus(developer, status);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by start date range
    @GetMapping("/start-date-range")
    public ResponseEntity<List<Project>> getProjectsByStartDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Project> projects = projectRepository.findByStartDateBetween(startDate, endDate);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by expected completion date range
    @GetMapping("/completion-date-range")
    public ResponseEntity<List<Project>> getProjectsByCompletionDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        try {
            List<Project> projects = projectRepository.findByExpectedCompletionBetween(startDate, endDate);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // READ - Get projects by construction progress range

    @GetMapping("/construction-progress-range")
    public ResponseEntity<List<Project>> getProjectsByConstructionProgressRange(
            @RequestParam("minProgress") Integer minProgress,
            @RequestParam("maxProgress") Integer maxProgress) {
        try {
            List<Project> projects = projectRepository.findByConstructionProgressBetween(minProgress, maxProgress);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by total units range
    @GetMapping("/total-units-range")
    public ResponseEntity<List<Project>> getProjectsByTotalUnitsRange(
            @RequestParam("minUnits") Integer minUnits,
            @RequestParam("maxUnits") Integer maxUnits) {
        try {
            List<Project> projects = projectRepository.findByTotalUnitsBetween(minUnits, maxUnits);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects with available units greater than
    @GetMapping("/available-units-greater-than/{units}")
    public ResponseEntity<List<Project>> getProjectsWithAvailableUnitsGreaterThan(@PathVariable("units") Integer units) {
        try {
            List<Project> projects = projectRepository.findByAvailableUnitsGreaterThan(units);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects with available units less than
    @GetMapping("/available-units-less-than/{units}")
    public ResponseEntity<List<Project>> getProjectsWithAvailableUnitsLessThan(@PathVariable("units") Integer units) {
        try {
            List<Project> projects = projectRepository.findByAvailableUnitsLessThan(units);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects with sold units greater than
    @GetMapping("/sold-units-greater-than/{units}")
    public ResponseEntity<List<Project>> getProjectsWithSoldUnitsGreaterThan(@PathVariable("units") Integer units) {
        try {
            List<Project> projects = projectRepository.findBySoldUnitsGreaterThan(units);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects with reserved units greater than
    @GetMapping("/reserved-units-greater-than/{units}")
    public ResponseEntity<List<Project>> getProjectsWithReservedUnitsGreaterThan(@PathVariable("units") Integer units) {
        try {
            List<Project> projects = projectRepository.findByReservedUnitsGreaterThan(units);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by price range
    @GetMapping("/price-range")
    public ResponseEntity<List<Project>> getProjectsByPriceRange(
            @RequestParam("minPrice") BigDecimal minPrice,
            @RequestParam("maxPrice") BigDecimal maxPrice) {
        try {
            List<Project> projects = projectRepository.findByPriceRange(minPrice, maxPrice);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by minimum price
    @GetMapping("/min-price/{minPrice}")
    public ResponseEntity<List<Project>> getProjectsByMinPrice(@PathVariable("minPrice") BigDecimal minPrice) {
        try {
            List<Project> projects = projectRepository.findByMinPriceGreaterThanEqual(minPrice);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by maximum price
    @GetMapping("/max-price/{maxPrice}")
    public ResponseEntity<List<Project>> getProjectsByMaxPrice(@PathVariable("maxPrice") BigDecimal maxPrice) {
        try {
            List<Project> projects = projectRepository.findByMaxPriceLessThanEqual(maxPrice);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Search projects by keyword
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<Project>> searchProjectsByKeyword(@PathVariable("keyword") String keyword) {
        try {
            List<Project> projects = projectRepository.searchByKeyword(keyword);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects by amenity
    @GetMapping("/amenity/{amenity}")
    public ResponseEntity<List<Project>> getProjectsByAmenity(@PathVariable("amenity") String amenity) {
        try {
            List<Project> projects = projectRepository.findByAmenity(amenity);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get overdue projects
    @GetMapping("/overdue")
    public ResponseEntity<List<Project>> getOverdueProjects() {
        try {
            List<Project> projects = projectRepository.findOverdueProjects();
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get projects completing before date
    @GetMapping("/completing-before/{date}")
    public ResponseEntity<List<Project>> getProjectsCompletingBefore(
            @PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<Project> projects = projectRepository.findProjectsCompletingBefore(date);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all cities
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getAllCities() {
        try {
            List<String> cities = projectRepository.findAllCities();
            if (cities.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(cities, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all states
    @GetMapping("/states")
    public ResponseEntity<List<String>> getAllStates() {
        try {
            List<String> states = projectRepository.findAllStates();
            if (states.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(states, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // READ - Get all developers
    @GetMapping("/developers")
    public ResponseEntity<List<String>> getAllDevelopers() {
        try {
            List<String> developers = projectRepository.findAllDevelopers();
            if (developers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(developers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update project by ID
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable("id") Long id, @RequestBody Project project) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();

                // Update fields
                projectToUpdate.setName(project.getName());
                projectToUpdate.setDescription(project.getDescription());
                projectToUpdate.setLocation(project.getLocation());
                projectToUpdate.setAddress(project.getAddress());
                projectToUpdate.setCity(project.getCity());
                projectToUpdate.setState(project.getState());
                projectToUpdate.setZipCode(project.getZipCode());
                projectToUpdate.setTotalUnits(project.getTotalUnits());
                projectToUpdate.setAvailableUnits(project.getAvailableUnits());
                projectToUpdate.setReservedUnits(project.getReservedUnits());
                projectToUpdate.setSoldUnits(project.getSoldUnits());
                projectToUpdate.setConstructionProgress(project.getConstructionProgress());
                projectToUpdate.setStatus(project.getStatus());
                projectToUpdate.setStartDate(project.getStartDate());
                projectToUpdate.setExpectedCompletion(project.getExpectedCompletion());
                projectToUpdate.setDeveloper(project.getDeveloper());
                projectToUpdate.setImages(project.getImages());
                projectToUpdate.setAmenities(project.getAmenities());
                projectToUpdate.setPriceRange(project.getPriceRange());

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Partial update project by ID
    @PatchMapping("/{id}")
    public ResponseEntity<Project> partialUpdateProject(@PathVariable("id") Long id, @RequestBody Project project) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();

                // Update only non-null fields
                if (project.getName() != null) {
                    projectToUpdate.setName(project.getName());
                }
                if (project.getDescription() != null) {
                    projectToUpdate.setDescription(project.getDescription());
                }
                if (project.getLocation() != null) {
                    projectToUpdate.setLocation(project.getLocation());
                }
                if (project.getAddress() != null) {
                    projectToUpdate.setAddress(project.getAddress());
                }
                if (project.getCity() != null) {
                    projectToUpdate.setCity(project.getCity());
                }
                if (project.getState() != null) {
                    projectToUpdate.setState(project.getState());
                }
                if (project.getZipCode() != null) {
                    projectToUpdate.setZipCode(project.getZipCode());
                }
                if (project.getTotalUnits() != null) {
                    projectToUpdate.setTotalUnits(project.getTotalUnits());
                }
                if (project.getAvailableUnits() != null) {
                    projectToUpdate.setAvailableUnits(project.getAvailableUnits());
                }
                if (project.getReservedUnits() != null) {
                    projectToUpdate.setReservedUnits(project.getReservedUnits());
                }
                if (project.getSoldUnits() != null) {
                    projectToUpdate.setSoldUnits(project.getSoldUnits());
                }
                if (project.getConstructionProgress() != null) {
                    projectToUpdate.setConstructionProgress(project.getConstructionProgress());
                }
                if (project.getStatus() != null) {
                    projectToUpdate.setStatus(project.getStatus());
                }
                if (project.getStartDate() != null) {
                    projectToUpdate.setStartDate(project.getStartDate());
                }
                if (project.getExpectedCompletion() != null) {
                    projectToUpdate.setExpectedCompletion(project.getExpectedCompletion());
                }
                if (project.getDeveloper() != null) {
                    projectToUpdate.setDeveloper(project.getDeveloper());
                }
                if (project.getImages() != null) {
                    projectToUpdate.setImages(project.getImages());
                }
                if (project.getAmenities() != null) {
                    projectToUpdate.setAmenities(project.getAmenities());
                }
                if (project.getPriceRange() != null) {
                    projectToUpdate.setPriceRange(project.getPriceRange());
                }

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update project status
    @PatchMapping("/{id}/status")
    public ResponseEntity<Project> updateProjectStatus(@PathVariable("id") Long id, @RequestParam("status") ProjectStatus status) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                projectToUpdate.setStatus(status);

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update construction progress
    @PatchMapping("/{id}/construction-progress")
    public ResponseEntity<Project> updateConstructionProgress(@PathVariable("id") Long id, @RequestParam("progress") Integer progress) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                projectToUpdate.setConstructionProgress(progress);

                // Auto-update status based on progress
                if (progress >= 100) {
                    projectToUpdate.setStatus(ProjectStatus.COMPLETED);
                } else if (progress > 0) {
                    projectToUpdate.setStatus(ProjectStatus.UNDER_CONSTRUCTION);
                }

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update available units
    @PatchMapping("/{id}/available-units")
    public ResponseEntity<Project> updateAvailableUnits(@PathVariable("id") Long id, @RequestParam("availableUnits") Integer availableUnits) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                projectToUpdate.setAvailableUnits(availableUnits);

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update sold units
    @PatchMapping("/{id}/sold-units")
    public ResponseEntity<Project> updateSoldUnits(@PathVariable("id") Long id, @RequestParam("soldUnits") Integer soldUnits) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                projectToUpdate.setSoldUnits(soldUnits);

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update reserved units
    @PatchMapping("/{id}/reserved-units")
    public ResponseEntity<Project> updateReservedUnits(@PathVariable("id") Long id, @RequestParam("reservedUnits") Integer reservedUnits) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                projectToUpdate.setReservedUnits(reservedUnits);

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Update expected completion date
    @PatchMapping("/{id}/expected-completion")
    public ResponseEntity<Project> updateExpectedCompletion(
            @PathVariable("id") Long id,
            @RequestParam("expectedCompletion") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expectedCompletion) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                projectToUpdate.setExpectedCompletion(expectedCompletion);

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Add amenity to project
    @PatchMapping("/{id}/add-amenity")
    public ResponseEntity<Project> addAmenityToProject(@PathVariable("id") Long id, @RequestParam("amenity") String amenity) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                if (projectToUpdate.getAmenities() != null && !projectToUpdate.getAmenities().contains(amenity)) {
                    projectToUpdate.getAmenities().add(amenity);
                }

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Remove amenity from project
    @PatchMapping("/{id}/remove-amenity")
    public ResponseEntity<Project> removeAmenityFromProject(@PathVariable("id") Long id, @RequestParam("amenity") String amenity) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                if (projectToUpdate.getAmenities() != null) {
                    projectToUpdate.getAmenities().remove(amenity);
                }

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Add image to project
    @PatchMapping("/{id}/add-image")
    public ResponseEntity<Project> addImageToProject(@PathVariable("id") Long id, @RequestParam("imageUrl") String imageUrl) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                if (projectToUpdate.getImages() != null && !projectToUpdate.getImages().contains(imageUrl)) {
                    projectToUpdate.getImages().add(imageUrl);
                }

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Remove image from project
    @PatchMapping("/{id}/remove-image")
    public ResponseEntity<Project> removeImageFromProject(@PathVariable("id") Long id, @RequestParam("imageUrl") String imageUrl) {
        try {
            Optional<Project> existingProject = projectRepository.findById(id);
            if (existingProject.isPresent()) {
                Project projectToUpdate = existingProject.get();
                if (projectToUpdate.getImages() != null) {
                    projectToUpdate.getImages().remove(imageUrl);
                }

                Project updatedProject = projectRepository.save(projectToUpdate);
                return new ResponseEntity<>(updatedProject, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete project by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteProject(@PathVariable("id") Long id) {
        try {
            Optional<Project> project = projectRepository.findById(id);
            if (project.isPresent()) {
                projectRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete all projects
    @DeleteMapping
    public ResponseEntity<HttpStatus> deleteAllProjects() {
        try {
            projectRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete projects by status
    @DeleteMapping("/status/{status}")
    public ResponseEntity<HttpStatus> deleteProjectsByStatus(@PathVariable("status") ProjectStatus status) {
        try {
            List<Project> projects = projectRepository.findByStatus(status);
            if (!projects.isEmpty()) {
                projectRepository.deleteAll(projects);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete projects by city
    @DeleteMapping("/city/{city}")
    public ResponseEntity<HttpStatus> deleteProjectsByCity(@PathVariable("city") String city) {
        try {
            List<Project> projects = projectRepository.findByCity(city);
            if (!projects.isEmpty()) {
                projectRepository.deleteAll(projects);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Delete projects by developer
    @DeleteMapping("/developer/{developer}")
    public ResponseEntity<HttpStatus> deleteProjectsByDeveloper(@PathVariable("developer") String developer) {
        try {
            List<Project> projects = projectRepository.findByDeveloper(developer);
            if (!projects.isEmpty()) {
                projectRepository.deleteAll(projects);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Additional utility endpoints
    // GET - Get project count
    @GetMapping("/count")
    public ResponseEntity<Long> getProjectCount() {
        try {
            long count = projectRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get project count by status
    @GetMapping("/count/status/{status}")
    public ResponseEntity<Long> getProjectCountByStatus(@PathVariable("status") ProjectStatus status) {
        try {
            Long count = projectRepository.countByStatus(status);
            return new ResponseEntity<>(count != null ? count : 0L, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total units by status
    @GetMapping("/total-units/status/{status}")
    public ResponseEntity<Integer> getTotalUnitsByStatus(@PathVariable("status") ProjectStatus status) {
        try {
            Integer totalUnits = projectRepository.getTotalUnitsByStatus(status);
            return new ResponseEntity<>(totalUnits != null ? totalUnits : 0, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total available units across all projects
    @GetMapping("/total-available-units")
    public ResponseEntity<Integer> getTotalAvailableUnits() {
        try {
            Integer totalAvailableUnits = projectRepository.getTotalAvailableUnits();
            return new ResponseEntity<>(totalAvailableUnits != null ? totalAvailableUnits : 0, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get total sold units across all projects
    @GetMapping("/total-sold-units")
    public ResponseEntity<Integer> getTotalSoldUnits() {
        try {
            Integer totalSoldUnits = projectRepository.getTotalSoldUnits();
            return new ResponseEntity<>(totalSoldUnits != null ? totalSoldUnits : 0, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get average construction progress
    @GetMapping("/average-construction-progress")
    public ResponseEntity<Double> getAverageConstructionProgress() {
        try {
            Double averageProgress = projectRepository.getAverageConstructionProgress();
            return new ResponseEntity<>(averageProgress != null ? averageProgress : 0.0, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get projects with high availability (more than 50% available)
    @GetMapping("/high-availability")
    public ResponseEntity<List<Project>> getProjectsWithHighAvailability() {
        try {
            List<Project> allProjects = projectRepository.findAll();
            List<Project> highAvailabilityProjects = allProjects.stream()
                    .filter(project -> project.getTotalUnits() != null && project.getAvailableUnits() != null)
                    .filter(project -> project.getTotalUnits() > 0)
                    .filter(project -> (double) project.getAvailableUnits() / project.getTotalUnits() > 0.5)
                    .toList();

            if (highAvailabilityProjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(highAvailabilityProjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get projects with low availability (less than 10% available)
    @GetMapping("/low-availability")
    public ResponseEntity<List<Project>> getProjectsWithLowAvailability() {
        try {
            List<Project> allProjects = projectRepository.findAll();
            List<Project> lowAvailabilityProjects = allProjects.stream()
                    .filter(project -> project.getTotalUnits() != null && project.getAvailableUnits() != null)
                    .filter(project -> project.getTotalUnits() > 0)
                    .filter(project -> (double) project.getAvailableUnits() / project.getTotalUnits() < 0.1)
                    .toList();

            if (lowAvailabilityProjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(lowAvailabilityProjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get projects nearing completion (90%+ progress)
    @GetMapping("/nearing-completion")
    public ResponseEntity<List<Project>> getProjectsNearingCompletion() {
        try {
            List<Project> projects = projectRepository.findByConstructionProgressBetween(90, 99);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get recently started projects (started within last 30 days)
    @GetMapping("/recently-started")
    public ResponseEntity<List<Project>> getRecentlyStartedProjects() {
        try {
            LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
            LocalDate today = LocalDate.now();
            List<Project> projects = projectRepository.findByStartDateBetween(thirtyDaysAgo, today);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get projects completing soon (within next 30 days)
    @GetMapping("/completing-soon")
    public ResponseEntity<List<Project>> getProjectsCompletingSoon() {
        try {
            LocalDate thirtyDaysFromNow = LocalDate.now().plusDays(30);
            List<Project> projects = projectRepository.findProjectsCompletingBefore(thirtyDaysFromNow);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get projects by multiple amenities
    @GetMapping("/amenities")
    public ResponseEntity<List<Project>> getProjectsByAmenities(@RequestParam("amenities") List<String> amenities) {
        try {
            List<Project> allProjects = projectRepository.findAll();
            List<Project> matchingProjects = allProjects.stream()
                    .filter(project -> project.getAmenities() != null)
                    .filter(project -> project.getAmenities().containsAll(amenities))
                    .toList();

            if (matchingProjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(matchingProjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get project statistics summary
    @GetMapping("/statistics")
    public ResponseEntity<ProjectStatistics> getProjectStatistics() {
        try {
            long totalProjects = projectRepository.count();
            Long planningCount = projectRepository.countByStatus(ProjectStatus.PLANNING);
            Long underConstructionCount = projectRepository.countByStatus(ProjectStatus.UNDER_CONSTRUCTION);
            Long completedCount = projectRepository.countByStatus(ProjectStatus.COMPLETED);
            Integer totalAvailableUnits = projectRepository.getTotalAvailableUnits();
            Integer totalSoldUnits = projectRepository.getTotalSoldUnits();
            Double averageProgress = projectRepository.getAverageConstructionProgress();

            ProjectStatistics stats = ProjectStatistics.builder()
                    .totalProjects(totalProjects)
                    .planningProjects(planningCount != null ? planningCount : 0L)
                    .underConstructionProjects(underConstructionCount != null ? underConstructionCount : 0L)
                    .completedProjects(completedCount != null ? completedCount : 0L)
                    .totalAvailableUnits(totalAvailableUnits != null ? totalAvailableUnits : 0)
                    .totalSoldUnits(totalSoldUnits != null ? totalSoldUnits : 0)
                    .averageConstructionProgress(averageProgress != null ? averageProgress : 0.0)
                    .build();

            return new ResponseEntity<>(stats, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get projects sorted by various criteria
    @GetMapping("/sorted")
    public ResponseEntity<List<Project>> getProjectsSorted(@RequestParam("sortBy") String sortBy, @RequestParam(value = "order", defaultValue = "asc") String order) {
        try {
            List<Project> projects = projectRepository.findAll();

            switch (sortBy.toLowerCase()) {
                case "name" ->
                    projects = projects.stream()
                            .sorted(order.equals("desc")
                                    ? (p1, p2) -> p2.getName().compareTo(p1.getName())
                                    : (p1, p2) -> p1.getName().compareTo(p2.getName()))
                            .toList();
                case "startdate" ->
                    projects = projects.stream()
                            .filter(p -> p.getStartDate() != null)
                            .sorted(order.equals("desc")
                                    ? (p1, p2) -> p2.getStartDate().compareTo(p1.getStartDate())
                                    : (p1, p2) -> p1.getStartDate().compareTo(p2.getStartDate()))
                            .toList();
                case "progress" ->
                    projects = projects.stream()
                            .filter(p -> p.getConstructionProgress() != null)
                            .sorted(order.equals("desc")
                                    ? (p1, p2) -> p2.getConstructionProgress().compareTo(p1.getConstructionProgress())
                                    : (p1, p2) -> p1.getConstructionProgress().compareTo(p2.getConstructionProgress()))
                            .toList();
                case "totalunits" ->
                    projects = projects.stream()
                            .filter(p -> p.getTotalUnits() != null)
                            .sorted(order.equals("desc")
                                    ? (p1, p2) -> p2.getTotalUnits().compareTo(p1.getTotalUnits())
                                    : (p1, p2) -> p1.getTotalUnits().compareTo(p2.getTotalUnits()))
                            .toList();
                case "availableunits" ->
                    projects = projects.stream()
                            .filter(p -> p.getAvailableUnits() != null)
                            .sorted(order.equals("desc")
                                    ? (p1, p2) -> p2.getAvailableUnits().compareTo(p1.getAvailableUnits())
                                    : (p1, p2) -> p1.getAvailableUnits().compareTo(p2.getAvailableUnits()))
                            .toList();
                case "soldunits" ->
                    projects = projects.stream()
                            .filter(p -> p.getSoldUnits() != null)
                            .sorted(order.equals("desc")
                                    ? (p1, p2) -> p2.getSoldUnits().compareTo(p1.getSoldUnits())
                                    : (p1, p2) -> p1.getSoldUnits().compareTo(p2.getSoldUnits()))
                            .toList();
                default -> {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
            }

            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET - Get projects with pagination
    @GetMapping("/paginated")
    public ResponseEntity<List<Project>> getProjectsPaginated(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        try {
            List<Project> allProjects = projectRepository.findAll();
            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, allProjects.size());

            if (startIndex >= allProjects.size()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<Project> paginatedProjects = allProjects.subList(startIndex, endIndex);
            return new ResponseEntity<>(paginatedProjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Bulk update project status
    @PatchMapping("/bulk-update-status")
    public ResponseEntity<List<Project>> bulkUpdateProjectStatus(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("status") ProjectStatus status) {
        try {
            List<Project> updatedProjects = ids.stream()
                    .map(id -> projectRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(project -> project.setStatus(status))
                    .map(project -> projectRepository.save(project))
                    .toList();

            if (updatedProjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedProjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // UPDATE - Bulk update construction progress
    @PatchMapping("/bulk-update-progress")
    public ResponseEntity<List<Project>> bulkUpdateConstructionProgress(
            @RequestParam("ids") List<Long> ids,
            @RequestParam("progress") Integer progress) {
        try {
            List<Project> updatedProjects = ids.stream()
                    .map(id -> projectRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .peek(project -> {
                        project.setConstructionProgress(progress);
                        // Auto-update status based on progress
                        if (progress >= 100) {
                            project.setStatus(ProjectStatus.COMPLETED);
                        } else if (progress > 0) {
                            project.setStatus(ProjectStatus.UNDER_CONSTRUCTION);
                        }
                    })
                    .map(project -> projectRepository.save(project))
                    .toList();

            if (updatedProjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(updatedProjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE - Bulk delete projects
    @DeleteMapping("/bulk-delete")
    public ResponseEntity<HttpStatus> bulkDeleteProjects(@RequestParam("ids") List<Long> ids) {
        try {
            List<Project> projectsToDelete = ids.stream()
                    .map(id -> projectRepository.findById(id))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .toList();

            if (projectsToDelete.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            projectRepository.deleteAll(projectsToDelete);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

// Helper class for project statistics
class ProjectStatistics {

    private Long totalProjects;
    private Long planningProjects;
    private Long underConstructionProjects;
    private Long completedProjects;
    private Integer totalAvailableUnits;
    private Integer totalSoldUnits;
    private Double averageConstructionProgress;

    // Constructor
    public ProjectStatistics() {
    }

    public ProjectStatistics(Long totalProjects, Long planningProjects, Long underConstructionProjects,
            Long completedProjects, Integer totalAvailableUnits, Integer totalSoldUnits,
            Double averageConstructionProgress) {
        this.totalProjects = totalProjects;
        this.planningProjects = planningProjects;
        this.underConstructionProjects = underConstructionProjects;
        this.completedProjects = completedProjects;
        this.totalAvailableUnits = totalAvailableUnits;
        this.totalSoldUnits = totalSoldUnits;
        this.averageConstructionProgress = averageConstructionProgress;
    }

    // Builder pattern
    public static ProjectStatisticsBuilder builder() {
        return new ProjectStatisticsBuilder();
    }

    public static class ProjectStatisticsBuilder {

        private Long totalProjects;
        private Long planningProjects;
        private Long underConstructionProjects;
        private Long completedProjects;
        private Integer totalAvailableUnits;
        private Integer totalSoldUnits;
        private Double averageConstructionProgress;

        public ProjectStatisticsBuilder totalProjects(Long totalProjects) {
            this.totalProjects = totalProjects;
            return this;
        }

        public ProjectStatisticsBuilder planningProjects(Long planningProjects) {
            this.planningProjects = planningProjects;
            return this;
        }

        public ProjectStatisticsBuilder underConstructionProjects(Long underConstructionProjects) {
            this.underConstructionProjects = underConstructionProjects;
            return this;
        }

        public ProjectStatisticsBuilder completedProjects(Long completedProjects) {
            this.completedProjects = completedProjects;
            return this;
        }

        public ProjectStatisticsBuilder totalAvailableUnits(Integer totalAvailableUnits) {
            this.totalAvailableUnits = totalAvailableUnits;
            return this;
        }

        public ProjectStatisticsBuilder totalSoldUnits(Integer totalSoldUnits) {
            this.totalSoldUnits = totalSoldUnits;
            return this;
        }

        public ProjectStatisticsBuilder averageConstructionProgress(Double averageConstructionProgress) {
            this.averageConstructionProgress = averageConstructionProgress;
            return this;
        }

        public ProjectStatistics build() {
            return new ProjectStatistics(totalProjects, planningProjects, underConstructionProjects,
                    completedProjects, totalAvailableUnits, totalSoldUnits,
                    averageConstructionProgress);
        }
    }

    // Getters and Setters
    public Long getTotalProjects() {
        return totalProjects;
    }

    public void setTotalProjects(Long totalProjects) {
        this.totalProjects = totalProjects;
    }

    public Long getPlanningProjects() {
        return planningProjects;
    }

    public void setPlanningProjects(Long planningProjects) {
        this.planningProjects = planningProjects;
    }

    public Long getUnderConstructionProjects() {
        return underConstructionProjects;
    }

    public void setUnderConstructionProjects(Long underConstructionProjects) {
        this.underConstructionProjects = underConstructionProjects;
    }

    public Long getCompletedProjects() {
        return completedProjects;
    }

    public void setCompletedProjects(Long completedProjects) {
        this.completedProjects = completedProjects;
    }

    public Integer getTotalAvailableUnits() {
        return totalAvailableUnits;
    }

    public void setTotalAvailableUnits(Integer totalAvailableUnits) {
        this.totalAvailableUnits = totalAvailableUnits;
    }

    public Integer getTotalSoldUnits() {
        return totalSoldUnits;
    }

    public void setTotalSoldUnits(Integer totalSoldUnits) {
        this.totalSoldUnits = totalSoldUnits;
    }

    public Double getAverageConstructionProgress() {
        return averageConstructionProgress;
    }

    public void setAverageConstructionProgress(Double averageConstructionProgress) {
        this.averageConstructionProgress = averageConstructionProgress;
    }
}
