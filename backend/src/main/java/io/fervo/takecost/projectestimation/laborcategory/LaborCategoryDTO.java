package io.fervo.takecost.projectestimation.laborcategory;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LaborCategoryDTO(
        @Schema(description = "Unique identifier for the labor category", example = "1")
        Long id,

        @Schema(description = "Name of the labor category", example = "Electrician")
        @NotBlank(message = "Labor category name cannot be blank")
        @Size(max = 255, message = "Labor category name cannot exceed 255 characters")
        String name,

        @Schema(description = "Description of the labor category", example = "Handles electrical work in projects")
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description
) {
}
