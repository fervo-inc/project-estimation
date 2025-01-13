package io.fervo.takecost.projectestimation.project;

import io.fervo.takecost.projectestimation.project.model.CostBreakdown;
import io.fervo.takecost.projectestimation.project.model.CostEstimate;
import io.fervo.takecost.projectestimation.project.model.Project;
import io.fervo.takecost.projectestimation.project.model.ProjectsSummary;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Projects", description = "Handle project-level operations")
public class ProjectController {
    private final ProjectService service;
    private final ProjectMapper projectMapper;

    @GetMapping
    @Operation(summary = "List all projects", description = "Fetch all projects with optional pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of projects")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String order) {
        var pageable = PageRequest.of(page, size, Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "name"));
        log.info("Fetching projects with pagination: {}", pageable);
        var projects = service.getAll(pageable).map(projectMapper::toDTO);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project by ID", description = "Retrieve a project by its unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<ProjectDTO> getProject(@PathVariable Long id) {
        var project = projectMapper.toDTO(service.getById(id));
        return ResponseEntity.ok(project);
    }

    @PostMapping
    @Operation(summary = "Create a new project", description = "Add a new project to the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectDTO> createProject(@Valid @RequestBody ProjectDTO projectDTO) {
        log.info("Creating project: {}", projectDTO);
        var project = projectMapper.toEntity(projectDTO);
        var savedProject = service.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMapper.toDTO(savedProject));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing project", description = "Modify the details of an existing project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTO projectDTO) {
        var project = projectMapper.toEntity(projectDTO);
        project.setId(id);
        var updatedProject = service.save(project);
        return ResponseEntity.ok(projectMapper.toDTO(updatedProject));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a project", description = "Remove a project from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/{projectId}/estimate")
    @Operation(summary = "Get project cost estimate", description = "Calculate the cost estimate for a specific project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cost estimate calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<CostEstimate> getCostEstimate(@PathVariable Long projectId) {
        var costEstimate = service.calculateCostEstimate(projectId);
        return ResponseEntity.ok(costEstimate);
    }

    @GetMapping("/summary")
    @Operation(summary = "Get project summary", description = "Fetch high-level metrics about all projects")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project summary retrieved successfully")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<ProjectsSummary> getProjectSummary() {
        var summary = service.getProjectSummary();
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/{projectId}/cost-breakdown")
    @Operation(summary = "Get project cost breakdown", description = "Fetch a breakdown of costs between materials and labor for a specific project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cost breakdown retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project not found")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<CostBreakdown> getCostBreakdown(@PathVariable Long projectId) {
        var breakdown = service.getCostBreakdown(projectId);
        return ResponseEntity.ok(breakdown);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming projects", description = "Fetch all projects with start dates in the future")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Upcoming projects retrieved successfully")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<List<Project>> getUpcomingProjects() {
        var upcomingProjects = service.getUpcomingProjects();
        return ResponseEntity.ok(upcomingProjects);
    }
}
