package io.fervo.takecost.projectestimation.projectlabor;

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
@RequestMapping("/api/v1/projects/{projectId}/labor")
@RequiredArgsConstructor
@Tag(name = "Project Labor", description = "Manage labor assignments for specific projects")
public class ProjectLaborController {
    private final ProjectLaborService service;
    private final ProjectLaborMapper projectLaborMapper;

    @GetMapping
    @Operation(summary = "List all labor for a project", description = "Fetch all labor entries linked to a project with optional pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of project labor entries")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<Page<ProjectLaborDTO>> getAllLaborByProject(
            @PathVariable Long projectId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String order) {
        var pageable = PageRequest.of(page, size, Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "id"));
        var labor = service.getAllByProjectId(projectId, pageable);
        log.info("getAllLaborByProject labor: {}", labor.getContent());
        var dtos = labor.map(projectLaborMapper::toDTO);
        log.info("getAllLaborByProject dtos: {}", dtos.getContent());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get project labor by ID", description = "Retrieve details of a specific project labor entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project labor retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Project labor not found")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<ProjectLaborDTO> getProjectLabor(@PathVariable Long id) {
        var labor = projectLaborMapper.toDTO(service.getById(id));
        return ResponseEntity.ok(labor);
    }

    @PostMapping
    @Operation(summary = "Add labor to a project", description = "Associate a labor category with a project")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project labor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectLaborDTO> createProjectLabor(
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectLaborDTO projectLaborDTO) {
        var projectLabor = projectLaborMapper.toEntity(projectLaborDTO);
        projectLabor.setId(projectId);
        var savedLabor = service.save(projectLabor);
        return ResponseEntity.status(HttpStatus.CREATED).body(projectLaborMapper.toDTO(savedLabor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update project labor", description = "Modify the details of an existing project labor entry")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project labor updated successfully"),
            @ApiResponse(responseCode = "404", description = "Project labor not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<ProjectLaborDTO> updateProjectLabor(
            @PathVariable Long id,
            @Valid @RequestBody ProjectLaborDTO projectLaborDTO) {
        var projectLabor = projectLaborMapper.toEntity(projectLaborDTO);
        projectLabor.setId(id);
        var updatedLabor = service.save(projectLabor);
        return ResponseEntity.ok(projectLaborMapper.toDTO(updatedLabor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remove labor from a project", description = "Delete a labor entry linked to a project")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project labor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Project labor not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Void> deleteProjectLabor(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.ok().build();
    }
}
