package io.fervo.takecost.projectestimation.projectmaterial;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProjectMaterialDTO(
        @Schema(description = "Unique identifier for the project material entry", example = "1")
        Long id,

        @Schema(description = "Associated project ID", example = "101")
        @NotNull(message = "Project ID cannot be null")
        Long projectId,

        @Schema(description = "Associated material ID", example = "202")
        @NotNull(message = "Material ID cannot be null")
        Long materialId,

        @Schema(description = "Quantity of the material", example = "100")
        @PositiveOrZero(message = "Quantity cannot be negative")
        Double quantity,

        @Schema(description = "Unit price of the material", example = "12.5")
        @PositiveOrZero(message = "Unit price cannot be negative")
        BigDecimal unitPrice,

        @Schema(description = "Additional notes for the project-material entry", example = "Urgent delivery required")
        @Size(max = 500, message = "Notes cannot exceed 500 characters")
        String notes
) {
}
