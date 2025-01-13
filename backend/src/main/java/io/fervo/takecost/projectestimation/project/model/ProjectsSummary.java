package io.fervo.takecost.projectestimation.project.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record ProjectsSummary(
        @Schema(description = "Total number of projects", example = "15")
        Long totalProjects,

        @Schema(description = "Total cost of materials", example = "50000.75")
        BigDecimal totalMaterialCost,

        @Schema(description = "Total cost of labor", example = "80000.50")
        BigDecimal totalLaborCost,

        @Schema(description = "Average project cost", example = "8667.37")
        BigDecimal averageCost
) {
}
