package io.fervo.takecost.projectestimation.controller;

import io.fervo.takecost.projectestimation.dto.ProjectDTO;
import io.fervo.takecost.projectestimation.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Projects", description = "Operations related to projects")
@RestController
@RequestMapping("/api/v1/projects")
@Validated
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @Operation(
            summary = "Create a new project",
            description = "Allows Admins and Project Managers to create a new project by providing its details."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project successfully created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid project details provided", content = @Content)
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER') or hasAuthority()")
    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Details of the new project to create",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDTO.class))
            )
            @RequestBody @Valid ProjectDTO projectDTO) {
        // Make sure the id is null otherwise the operation will fail
        if (projectDTO.id() != null) {
            throw new IllegalArgumentException("Project ID must not be set when creating a new project");
        }
        var createdProject = projectService.createProject(projectDTO);
        return ResponseEntity.status(201).body(ProjectDTO.fromProject(createdProject));
    }

    @Operation(
            summary = "Retrieve all projects",
            description = "Fetch a paginated list of all existing projects, with optional page and size parameters."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of projects", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Page.class))
            }),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Page<ProjectDTO>> getAllProjects(
            @Parameter(description = "Page number, starting from 0", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @Parameter(description = "Number of items per page, minimum is 1", example = "10")
            @RequestParam(defaultValue = "10") @Min(1) int size
    ) {
        var pageRequest = PageRequest.of(page, size);
        var projects = projectService.getAllProjects(pageRequest);
        Page<ProjectDTO> dtos = projects.map(ProjectDTO::fromProject);
        return ResponseEntity.ok(dtos);
    }

    @Operation(
            summary = "Update an existing project",
            description = "Update the details of an existing project by providing its ID and the new data."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project successfully updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid project data provided for update", content = @Content)
    })
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDTO> updateProject(
            @Parameter(description = "The unique identifier of the project to update", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID projectId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "The updated project details",
                    required = true,
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProjectDTO.class))
            )
            @RequestBody @Valid ProjectDTO projectDTO) {
        var updatedProject = projectService.updateProject(projectId, projectDTO);
        return ResponseEntity.ok(ProjectDTO.fromProject(updatedProject));
    }

    @Operation(
            summary = "Delete a project",
            description = "Delete an existing project using its unique ID. Once deleted, it cannot be recovered."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project successfully deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Project not found", content = @Content)
    })
    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(
            @Parameter(description = "The unique identifier of the project to delete", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID projectId) {
        projectService.deleteProject(projectId);
        return ResponseEntity.ok("Project deleted successfully");
    }
}