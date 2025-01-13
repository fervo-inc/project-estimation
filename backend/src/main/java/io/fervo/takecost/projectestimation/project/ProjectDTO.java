package io.fervo.takecost.projectestimation.project;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ProjectDTO(
        @Schema(description = "Unique identifier for the project", example = "1")
        Long id,

        @Schema(description = "Name of the project", example = "Skyline Construction")
        @NotBlank(message = "Project name cannot be blank")
        @Size(max = 255, message = "Project name cannot exceed 255 characters")
        String name,

        @Schema(description = "Description of the project", example = "High-rise building project")
        @Size(max = 500, message = "Description cannot exceed 500 characters")
        String description,

        @Schema(description = "Location of the project", example = "Downtown Toronto")
        @NotBlank(message = "Location cannot be blank")
        @Size(max = 255, message = "Location cannot exceed 255 characters")
        String location,

        @Schema(description = "Start date of the project", example = "2025-01-01")
        @NotNull(message = "Start date is required")
        LocalDate startDate,

        @Schema(description = "End date of the project", example = "2025-12-31")
        @NotNull(message = "End date is required")
        LocalDate endDate,

        @Schema(description = "Project status", example = "PLANNED", allowableValues = {"PLANNED", "IN_PROGRESS", "COMPLETED"}, defaultValue = "PLANNED")
        @NotNull(message = "Status is required")
        ProjectStatus status
) {
}
