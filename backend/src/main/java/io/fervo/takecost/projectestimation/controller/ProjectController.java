package io.fervo.takecost.projectestimation.controller;

import io.fervo.takecost.projectestimation.dto.ProjectDTO;
import io.fervo.takecost.projectestimation.service.ProjectService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/projects")
@Validated
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(@RequestBody @Valid ProjectDTO projectDTO) {
        var createdProject = projectService.createProject(projectDTO);
        return ResponseEntity.status(201).body(ProjectDTO.fromProject(createdProject));
    }

    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        var pageRequest = PageRequest.of(page, size);
        var projects = projectService.getAllProjects(pageRequest);
        Page<ProjectDTO> dtos = projects.map(ProjectDTO::fromProject);
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(
            @PathVariable UUID projectId,
            @RequestBody @Valid ProjectDTO projectDTO
    ) {
        var updatedProject = projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok("Project deleted successfully");
    }
}
