package io.fervo.takecost.projectestimation.material;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record MaterialCatalogDTO(
        @Schema(description = "Unique identifier for the material", example = "1")
        Long id,

        @Schema(description = "Material's name", example = "Concrete")
        @NotBlank(message = "Material name cannot be blank")
        @Size(max = 255, message = "Material name cannot exceed 255 characters")
        String name,

        @Size(max = 500, message = "Material description cannot exceed 255 characters")
        String description,

        @Schema(description = "Material's category", example = "Building Materials")
        @NotBlank(message = "Category cannot be blank")
        @Size(max = 255, message = "Category cannot exceed 255 characters")
        String category,

        @Schema(description = "Material's sub-category", example = "Building Materials")
        @Size(max = 255, message = "Sub-category cannot exceed 255 characters")
        String subCategory,

        @Schema(description = "Material's stock quantity", example = "100")
        @PositiveOrZero(message = "Stock quantity cannot be negative")
        Integer inStock,

        @Schema(description = "Material's lead time in days", example = "10")
        @PositiveOrZero(message = "Lead time cannot be negative")
        Integer leadTimeDays,

        @Schema(description = "Material's unit type", example = "Kilogram")
        @NotBlank(message = "Unit type cannot be blank")
        @Size(max = 100, message = "Unit type cannot exceed 100 characters")
        String unitType,

        @Schema(description = "Price per unit", example = "12.5")
        @PositiveOrZero(message = "Unit price cannot be negative")
        Double unitPrice,

        @Schema(description = "Material's vendor ID", example = "5")
        Long vendorId
) {
}
