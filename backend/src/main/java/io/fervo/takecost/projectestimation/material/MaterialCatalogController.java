package io.fervo.takecost.projectestimation.material;

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
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
@Tag(name = "Materials", description = "Manage the catalog of materials")
public class MaterialCatalogController {
    private final MaterialCatalogService service;
    private final MaterialCatalogMapper materialCatalogMapper;

    @GetMapping
    @Operation(summary = "List all materials", description = "Fetch all materials with optional pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of materials")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<Page<MaterialCatalogDTO>> getAllMaterials(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String order) {
        var pageable = PageRequest.of(page, size, Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "name"));
        var materials = service.getAll(pageable).map(materialCatalogMapper::toDTO);
        return ResponseEntity.ok(materials);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get material by ID", description = "Retrieve a material by its unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Material not found")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<MaterialCatalogDTO> getMaterial(@PathVariable Long id) {
        var material = materialCatalogMapper.toDTO(service.getById(id));
        return ResponseEntity.ok(material);
    }

    @PostMapping
    @Operation(summary = "Create a new material", description = "Add a new material to the catalog")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Material created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<MaterialCatalogDTO> createMaterial(@Valid @RequestBody MaterialCatalogDTO materialDTO) {
        var material = materialCatalogMapper.toEntity(materialDTO);
        var savedMaterial = service.save(material);
        return ResponseEntity.status(HttpStatus.CREATED).body(materialCatalogMapper.toDTO(savedMaterial));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing material", description = "Modify the details of an existing material")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Material updated successfully"),
            @ApiResponse(responseCode = "404", description = "Material not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<MaterialCatalogDTO> updateMaterial(@PathVariable Long id, @Valid @RequestBody MaterialCatalogDTO materialDTO) {
        var material = materialCatalogMapper.toEntity(materialDTO);
        material.setId(id);
        var updatedMaterial = service.save(material);
        return ResponseEntity.ok(materialCatalogMapper.toDTO(updatedMaterial));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a material", description = "Remove a material from the catalog")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Material deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Material not found")
    })
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
