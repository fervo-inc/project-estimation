package io.fervo.takecost.projectestimation.service;

import io.fervo.takecost.projectestimation.dto.ProjectDTO;
import io.fervo.takecost.projectestimation.model.Project;
import io.fervo.takecost.projectestimation.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        if (projectRepository.findByName(projectDTO.name()).isPresent()) {
            throw new IllegalArgumentException("Project with the same name already exists");
        }

        var project = new Project(
                null,
                projectDTO.name(),
                projectDTO.description(),
                projectDTO.location(),
                projectDTO.startDate(),
                projectDTO.endDate()
        );
        var savedProject = projectRepository.save(project);
        return new ProjectDTO(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getLocation(),
                savedProject.getStartDate(),
                savedProject.getEndDate()
        );
    }

    public Page<ProjectDTO> getAllProjects(PageRequest pageRequest) {
        return projectRepository.findAll(pageRequest)
                .map(project -> new ProjectDTO(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getLocation(),
                        project.getStartDate(),
                        project.getEndDate()
                ));
    }

    public ProjectDTO updateProject(UUID id, ProjectDTO projectDTO) {
        var project = projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        project.setName(projectDTO.name());
        project.setDescription(projectDTO.description());
        project.setLocation(projectDTO.location());
        project.setStartDate(projectDTO.startDate());
        project.setEndDate(projectDTO.endDate());
        var updatedProject = projectRepository.save(project);
        return new ProjectDTO(
                updatedProject.getId(),
                updatedProject.getName(),
                updatedProject.getDescription(),
                updatedProject.getLocation(),
                updatedProject.getStartDate(),
                updatedProject.getEndDate()
        );
    }

    public void deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
