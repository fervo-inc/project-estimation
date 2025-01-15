package io.fervo.takecost.projectestimation.vendor;

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
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
@Tag(name = "Vendors", description = "Manage vendors who supply materials")
public class VendorController {
    private final VendorService vendorService;
    private final VendorMapper vendorMapper;

    @GetMapping
    @Operation(summary = "List all vendors", description = "Fetch all vendors with optional pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of vendors")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<Page<VendorDTO>> getAllVendors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String order) {
        var pageable = PageRequest.of(page, size, Sort.by(order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "name"));
        log.debug("getAllVendors: {}", pageable);
        var vendors = vendorService.getAllVendors(pageable);
        log.debug("getAllVendors - vendor models: {}", vendors.getContent());
        var dtos = vendors.map(vendorMapper::toDTO);
        log.debug("getAllVendors - vendor dtos: {}", dtos.getContent());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vendor by ID", description = "Retrieve a vendor by their unique ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendor retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Vendor not found")
    })
    @PreAuthorize("isFullyAuthenticated()")
    public ResponseEntity<VendorDTO> getVendor(@PathVariable Long id) {
        var vendor = vendorMapper.toDTO(vendorService.getVendorById(id));
        return ResponseEntity.ok(vendor);
    }

    @PostMapping
    @Operation(summary = "Create a new vendor", description = "Add a new vendor to the system")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Vendor created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorDTO> createVendor(@Valid @RequestBody VendorDTO vendorDTO) {
        var vendor = vendorMapper.toEntity(vendorDTO);
        var savedVendor = vendorService.saveVendor(vendor);
        return ResponseEntity.status(HttpStatus.CREATED).body(vendorMapper.toDTO(savedVendor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing vendor", description = "Modify the details of an existing vendor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Vendor updated successfully"),
            @ApiResponse(responseCode = "404", description = "Vendor not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VendorDTO> updateVendor(@PathVariable Long id, @Valid @RequestBody VendorDTO vendorDTO) {
        var vendor = vendorMapper.toEntity(vendorDTO);
        vendor.setId(id);
        var updatedVendor = vendorService.saveVendor(vendor);
        return ResponseEntity.ok(vendorMapper.toDTO(updatedVendor));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a vendor", description = "Remove a vendor from the system")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Vendor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Vendor not found")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteVendor(@PathVariable Long id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}
