package io.fervo.takecost.projectestimation.projectmaterial;

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

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/projects/{projectId}/materials")
@RequiredArgsConstructor
@Tag(name = "Project Materials", description = "Manage materials associated with specific projects")
public class ProjectMaterialController {
    private final ProjectMaterialService service;
    private final ProjectMaterialMapper projectMaterialMapper;

    @GetMapping
    @Operation(summary = "List materials for a project", description = "Fetch all materials linked to a project with optional pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of project materials")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<Page<ProjectMaterialDTO>> getAllMaterialsByProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String order) {
        var pageable = PageRequest.of(page, size, Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "id"));
        var materials = service.getAllByProjectId(projectId, pageable).map(projectMaterialMapper::toDTO);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project material by ID", description = "Retrieve details of a specific project-material entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project material retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project material not found")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<ProjectMaterialDTO> getProjectMaterial(@PathVariable Long id) {
        var projectMaterial = projectMaterialMapper.toDTO(service.getById(id));
        return ResponseEntity.ok(projectMaterial);
    }

    @PostMapping
    @Operation(summary = "Add material to a project", description = "Associate a material with a project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project material created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectMaterialDTO> createProjectMaterial(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectMaterialDTO projectMaterialDTO) {
        var projectMaterial = projectMaterialMapper.toEntity(projectMaterialDTO);
        projectMaterial.setId(projectId);
        var savedMaterial = service.save(projectMaterial);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectMaterialMapper.toDTO(savedMaterial));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project material", description = "Modify the details of an existing project-material entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project material updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project material not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectMaterialDTO> updateProjectMaterial(
            @PathVariable Long id,
            @Valid @RequestBody ProjectMaterialDTO projectMaterialDTO) {
        var projectMaterial = projectMaterialMapper.toEntity(projectMaterialDTO);
        projectMaterial.setId(id);
        var updatedMaterial = service.save(projectMaterial);
        return ResponseEntity.ok(projectMaterialMapper.toDTO(updatedMaterial));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove material from project", description = "Delete a material linked to a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project material deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project material not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Void> deleteProjectMaterial(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
