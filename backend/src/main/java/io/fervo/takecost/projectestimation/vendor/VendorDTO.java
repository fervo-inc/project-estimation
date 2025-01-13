package io.fervo.takecost.projectestimation.vendor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record VendorDTO(
        @Schema(description = "Unique identifier for the vendor", example = "1")
        Long id,

        @Schema(description = "Vendor's name", example = "Acme Supplies")
        @NotBlank(message = "Vendor name cannot be blank")
        @Size(max = 255, message = "Vendor name cannot exceed 255 characters")
        String name,

        @Schema(description = "Email", example = "contact@acmesupplies.com")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        String email,

        @Schema(description = "Phone", example = "+1 (555) 870-0000")
        @Size(max = 255, message = "Phone cannot exceed 255 characters")
        String phone,

        @Schema(description = "Address of the vendor", example = "123 Main St, Toronto, ON")
        @Size(max = 255, message = "Address cannot exceed 255 characters")
        String address
) {
}
