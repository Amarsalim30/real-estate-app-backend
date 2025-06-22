
// @Service
// @Transactional
// public class ProjectService {

//     @Autowired
//     private ProjectRepository projectRepository;

//     public Project createProject(Project project) {
//         // Business logic validation
//         validateProject(project);
//         return projectRepository.save(project);
//     }

//     public void updateProjectProgress(Long projectId, float progress) {
//         Project project = projectRepository.findById(projectId)
//                 .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

//         project.setConstructionProgress(progress);

//         // Auto-complete project if progress is 100% and admin signed off
//         if (progress >= 100 && project.isAdminSignedOff()) {
//             project.setStatus(ProjectStatus.COMPLETED);
//             project.setCompletionDate(LocalDateTime.now());
//         }

//         projectRepository.save(project);
//     }

//     private void validateProject(Project project) {
//         // Add validation logic
//     }

        // Calculate project completion percentage including admin override
// public float getActualProgress(Project project) {
//     return project.isAdminSignedOff() ? 100f : project.getConstructionProgress();
// }

// // Check if project is overdue
// public boolean isProjectOverdue(Project project) {
//     return project.getTargetCompletionDate() != null && 
//            LocalDateTime.now().isAfter(project.getTargetCompletionDate()) &&
//            project.getStatus() != ProjectStatus.COMPLETED;
// }

// }
