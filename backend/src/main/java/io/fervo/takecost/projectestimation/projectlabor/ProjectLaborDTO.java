package io.fervo.takecost.projectestimation.projectlabor;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProjectLaborDTO(
        @Schema(description = "Unique identifier for the project labor entry", example = "1")
        Long id,

        @Schema(description = "Associated project ID", example = "101")
        @NotNull(message = "Project ID cannot be null")
        Long projectId,

        @Schema(description = "Associated labor category ID", example = "5")
        @NotNull(message = "Labor category ID cannot be null")
        Long laborCategoryId,

        @Schema(description = "Hourly rate for the labor", example = "25.0")
        @PositiveOrZero(message = "Hourly rate cannot be negative")
        Double hourlyRate,

        @Schema(description = "Estimated hours of labor", example = "80")
        @PositiveOrZero(message = "Estimated hours cannot be negative")
        Double estimatedHours,

        @Schema(description = "Notes for the project labor entry", example = "Work required on weekends")
        @Size(max = 500, message = "Notes cannot exceed 500 characters")
        String notes
) {
}
