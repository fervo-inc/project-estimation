package io.fervo.takecost.projectestimation.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ProjectDTO(
        UUID id,
        @NotBlank String name,
        String description,
        @NotBlank String location,
        @NotBlank String startDate,
        String endDate
) {
}
