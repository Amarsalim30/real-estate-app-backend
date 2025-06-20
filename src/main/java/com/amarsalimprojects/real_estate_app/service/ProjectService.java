package com.amarsalimprojects.real_estate_app.service;

import com.amarsalimprojects.real_estate_app.dto.ProjectDTO;
import com.amarsalimprojects.real_estate_app.entity.Project;
import com.amarsalimprojects.real_estate_app.mapper.ProjectMapper;
import com.amarsalimprojects.real_estate_app.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectMapper projectMapper;

    public List<ProjectDTO> getAllProjects() {
        List<Project> projects = projectRepository.findAll();
        return projects.stream()
                .map(projectMapper::toProjectDTO)
                .toList();
    }
}
