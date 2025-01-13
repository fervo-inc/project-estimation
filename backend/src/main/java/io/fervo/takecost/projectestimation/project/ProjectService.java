package io.fervo.takecost.projectestimation.project;

import io.fervo.takecost.projectestimation.project.model.CostBreakdown;
import io.fervo.takecost.projectestimation.project.model.CostEstimate;
import io.fervo.takecost.projectestimation.project.model.Project;
import io.fervo.takecost.projectestimation.project.model.ProjectsSummary;
import io.fervo.takecost.projectestimation.projectlabor.ProjectLaborService;
import io.fervo.takecost.projectestimation.projectmaterial.ProjectMaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository repository;
    private final ProjectMaterialService projectMaterialService;
    private final ProjectLaborService projectLaborService;
    private final ProjectMaterialService materialService;
    private final ProjectLaborService laborService;

    public Project save(Project project) {
        if (repository.findByName(project.getName()).isPresent()) {
            throw new IllegalArgumentException("Project with the same name already exists");
        }
        return repository.save(project);
    }

    public Project getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Project> getAll(Pageable pageable) {
        log.info("Getting all projects");
        var projects = repository.findAll(pageable);
        log.info("Found {} projects", projects.getTotalElements());
        return projects;
    }

    // # Estimates

    public CostEstimate calculateCostEstimate(Long projectId) {
        return repository.calculateCostEstimate(projectId);
    }

    public CostBreakdown getCostBreakdown(Long projectId) {
        return repository.getCostBreakdown(projectId);
    }

    public List<Project> getUpcomingProjects() {
        return repository.findByStartDateAfter(LocalDate.now());
    }

    // global metrics
    public ProjectsSummary getProjectSummary() {
        return repository.getProjectSummary();
    }
}
