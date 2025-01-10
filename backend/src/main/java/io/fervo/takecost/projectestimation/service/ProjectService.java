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

    public Project createProject(ProjectDTO projectDTO) {
        if (projectRepository.findByName(projectDTO.name()).isPresent()) {
            throw new IllegalArgumentException("Project with the same name already exists");
        }

        var project = projectDTO.toProject();
        return projectRepository.save(project);
    }

    public Page<Project> getAllProjects(PageRequest pageRequest) {
        return projectRepository.findAll(pageRequest);
    }

    public Project updateProject(UUID id, ProjectDTO projectDTO) {
        projectRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        return projectRepository.save(projectDTO.toProject());
    }

    public void deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new IllegalArgumentException("Project not found");
        }
        projectRepository.deleteById(id);
    }
}
