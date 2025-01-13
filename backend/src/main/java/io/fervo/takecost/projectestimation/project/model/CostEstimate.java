package io.fervo.takecost.projectestimation.project.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record CostEstimate(
        @Schema(description = "Total cost of materials", example = "1500.75")
        BigDecimal totalMaterialCost,

        @Schema(description = "Total cost of labor", example = "3000.50")
        BigDecimal totalLaborCost,

        @Schema(description = "Overall project cost", example = "4501.25")
        BigDecimal totalCost
) {
}
