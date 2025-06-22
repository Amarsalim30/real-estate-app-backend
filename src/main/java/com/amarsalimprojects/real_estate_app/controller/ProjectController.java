package com.amarsalimprojects.real_estate_app.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import com.amarsalimprojects.real_estate_app.model.Project;
import com.amarsalimprojects.real_estate_app.model.enums.ProjectStatus;
import com.amarsalimprojects.real_estate_app.repository.ProjectRepository;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "*")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

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
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
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

    // READ - Get projects by county
    @GetMapping("/county/{county}")
    public ResponseEntity<List<Project>> getProjectsByCounty(@PathVariable("county") String county) {
        try {
            List<Project> projects = projectRepository.findByCountyIgnoreCase(county);
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
            List<Project> projects = projectRepository.findByDeveloperNameIgnoreCase(developer);
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
            List<Project> projects = projectRepository.findByMinPriceGreaterThanEqualAndMaxPriceLessThanEqual(minPrice, maxPrice);
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(projects, HttpStatus.OK);
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

                // Update fields that exist in the model
                projectToUpdate.setName(project.getName());
                projectToUpdate.setDescription(project.getDescription());
                projectToUpdate.setAddress(project.getAddress());
                projectToUpdate.setCounty(project.getCounty());
                projectToUpdate.setSubCounty(project.getSubCounty());
                projectToUpdate.setStatus(project.getStatus());
                projectToUpdate.setDeveloperName(project.getDeveloperName());
                projectToUpdate.setConstructionProgress(project.getConstructionProgress());
                projectToUpdate.setStartDate(project.getStartDate());
                projectToUpdate.setTargetCompletionDate(project.getTargetCompletionDate());
                projectToUpdate.setCompletionDate(project.getCompletionDate());
                projectToUpdate.setAdminSignedOff(project.isAdminSignedOff());
                projectToUpdate.setMinPrice(project.getMinPrice());
                projectToUpdate.setMaxPrice(project.getMaxPrice());
                projectToUpdate.setImages(project.getImages());
                projectToUpdate.setAmenities(project.getAmenities());

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
                if (project.getAddress() != null) {
                    projectToUpdate.setAddress(project.getAddress());
                }
                if (project.getCounty() != null) {
                    projectToUpdate.setCounty(project.getCounty());
                }
                if (project.getSubCounty() != null) {
                    projectToUpdate.setSubCounty(project.getSubCounty());
                }
                if (project.getStatus() != null) {
                    projectToUpdate.setStatus(project.getStatus());
                }
                if (project.getDeveloperName() != null) {
                    projectToUpdate.setDeveloperName(project.getDeveloperName());
                }
                if (project.getConstructionProgress() != 0) {
                    projectToUpdate.setConstructionProgress(project.getConstructionProgress());
                }
                if (project.getStartDate() != null) {
                    projectToUpdate.setStartDate(project.getStartDate());
                }
                if (project.getTargetCompletionDate() != null) {
                    projectToUpdate.setTargetCompletionDate(project.getTargetCompletionDate());
                }
                if (project.getCompletionDate() != null) {
                    projectToUpdate.setCompletionDate(project.getCompletionDate());
                }
                if (project.getMinPrice() != null) {
                    projectToUpdate.setMinPrice(project.getMinPrice());
                }
                if (project.getMaxPrice() != null) {
                    projectToUpdate.setMaxPrice(project.getMaxPrice());
                }
                if (project.getImages() != null) {
                    projectToUpdate.setImages(project.getImages());
                }
                if (project.getAmenities() != null) {
                    projectToUpdate.setAmenities(project.getAmenities());
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
    public ResponseEntity<Project> updateConstructionProgress(@PathVariable("id") Long id, @RequestParam("progress") float progress) {
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

    // DELETE - Delete project by ID
    @DeleteMapping("/{id}")
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

    // Utility endpoint - Get project count
    @GetMapping("/count")
    public ResponseEntity<Long> getProjectCount() {
        try {
            long count = projectRepository.count();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 
    // GET - Get projects with units summary
    @GetMapping("/{id}/units-summary")
    public ResponseEntity<ProjectUnitsDTO> getProjectUnitsummary(@PathVariable Long id) {
        // Return project with units count, available, sold, etc.
    }

// GET - Get project dashboard data
    @GetMapping("/dashboard")
    public ResponseEntity<ProjectDashboardDTO> getProjectDashboard() {
        // Return summary statistics for dashboard
    }

// PATCH - Admin sign-off for project completion
    @PatchMapping("/{id}/admin-signoff")
    public ResponseEntity<Project> adminSignOff(@PathVariable Long id) {
        // Set adminSignedOff to true and update status if progress is 100%
    }

// GET - Get overdue projects
    @GetMapping("/overdue")
    public ResponseEntity<List<Project>> getOverdueProjects() {
        List<Project> projects = projectRepository.findOverdueProjects(LocalDateTime.now());
        return ResponseEntity.ok(projects);
    }

// GET - Get projects by amenity
    @GetMapping("/amenity/{amenity}")
    public ResponseEntity<List<Project>> getProjectsByAmenity(@PathVariable String amenity) {
        List<Project> allProjects = projectRepository.findAll();
        List<Project> filteredProjects = allProjects.stream()
                .filter(p -> p.getAmenities() != null && p.getAmenities().contains(amenity))
                .toList();
        return ResponseEntity.ok(filteredProjects);
    }
     */
    @GetMapping("/paginated")
    public ResponseEntity<Page<Project>> getProjectsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Project> projects = projectRepository.findAll(pageable);

        return ResponseEntity.ok(projects);
    }

}
