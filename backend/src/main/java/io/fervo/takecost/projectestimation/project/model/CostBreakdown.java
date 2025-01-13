package io.fervo.takecost.projectestimation.project.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CostBreakdown(
        @Schema(description = "Total material cost", example = "1500.75")
        BigDecimal totalMaterialCost,

        @Schema(description = "Total labor cost", example = "3000.50")
        BigDecimal totalLaborCost
) {
}
