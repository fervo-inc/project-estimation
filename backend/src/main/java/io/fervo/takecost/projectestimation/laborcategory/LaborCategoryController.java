package io.fervo.takecost.projectestimation.laborcategory;

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
@RequestMapping("/api/v1/labor-categories")
@RequiredArgsConstructor
@Tag(name = "Labor Categories", description = "Define categories for different types of labor")
public class LaborCategoryController {
    private final LaborCategoryService service;
    private final LaborCategoryMapper laborCategoryMapper;

    @GetMapping
    @Operation(summary = "List all labor categories", description = "Fetch all labor categories with optional pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of labor categories")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<Page<LaborCategoryDTO>> getAllLaborCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String order) {
        var pageable = PageRequest.of(page, size, Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "name"));
        var laborCategories = service.getAll(pageable).map(laborCategoryMapper::toDTO);
        return ResponseEntity.ok(laborCategories);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get labor category by ID", description = "Retrieve a labor category by its unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Labor category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Labor category not found")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<LaborCategoryDTO> getLaborCategory(@PathVariable Long id) {
        var laborCategory = laborCategoryMapper.toDTO(service.getById(id));
        return ResponseEntity.ok(laborCategory);
    }

    @PostMapping
    @Operation(summary = "Create a new labor category", description = "Add a new labor category to the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Labor category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<LaborCategoryDTO> createLaborCategory(@Valid @RequestBody LaborCategoryDTO laborCategoryDTO) {
        var laborCategory = laborCategoryMapper.toEntity(laborCategoryDTO);
        var savedLaborCategory = service.save(laborCategory);
        return ResponseEntity.status(HttpStatus.CREATED).body(laborCategoryMapper.toDTO(savedLaborCategory));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing labor category", description = "Modify the details of an existing labor category")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Labor category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Labor category not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<LaborCategoryDTO> updateLaborCategory(@PathVariable Long id, @Valid @RequestBody LaborCategoryDTO laborCategoryDTO) {
        var laborCategory = laborCategoryMapper.toEntity(laborCategoryDTO);
        laborCategory.setId(id);
        var updatedLaborCategory = service.save(laborCategory);
        return ResponseEntity.ok(laborCategoryMapper.toDTO(updatedLaborCategory));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a labor category", description = "Remove a labor category from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Labor category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Labor category not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Void> deleteLaborCategory(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
