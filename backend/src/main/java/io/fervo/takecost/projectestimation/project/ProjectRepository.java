package io.fervo.takecost.projectestimation.project;

import io.fervo.takecost.projectestimation.project.model.CostBreakdown;
import io.fervo.takecost.projectestimation.project.model.CostEstimate;
import io.fervo.takecost.projectestimation.project.model.Project;
import io.fervo.takecost.projectestimation.project.model.ProjectsSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);

    List<Project> findByStartDateAfter(LocalDate date);

    @Query(value = "SELECT CAST(COALESCE(SUM(pm.quantity * pm.unit_price), 0) AS DECIMAL(19,2)), " +
            "CAST(COALESCE(SUM(pl.hourly_rate * pl.estimated_hours), 0) AS DECIMAL(19,2)), " +
            "CAST(COALESCE(SUM(pm.quantity * pm.unit_price) + SUM(pl.hourly_rate * pl.estimated_hours), 0) AS DECIMAL(19,2)) " +
            "FROM projects p " +
            "LEFT JOIN project_materials pm ON p.id = pm.project_id " +
            "LEFT JOIN project_labor pl ON p.id = pl.project_id " +
            "WHERE p.id = :projectId", nativeQuery = true)
    CostEstimate calculateCostEstimate(@Param("projectId") Long projectId);

    @Query(value = """
                SELECT 
                    COUNT(p.id) AS totalProjects,
                    CAST(COALESCE(SUM(pm.quantity * pm.unit_price), 0) AS DECIMAL(19,2)) AS totalMaterialCost,
                    CAST(COALESCE(SUM(pl.hourly_rate * pl.estimated_hours), 0) AS DECIMAL(19,2)) AS totalLaborCost,
                    CAST(COALESCE((SUM(pm.quantity * pm.unit_price) + SUM(pl.hourly_rate * pl.estimated_hours)) / COUNT(p.id), 0) AS DECIMAL(19,2)) AS averageCost
                FROM projects p
                LEFT JOIN project_materials pm ON p.id = pm.project_id
                LEFT JOIN project_labor pl ON p.id = pl.project_id
            """, nativeQuery = true)
    ProjectsSummary getProjectSummary();

    @Query(value = """
                SELECT 
                    CAST(COALESCE(SUM(pm.quantity * pm.unit_price), 0) AS DECIMAL(19,2)) AS totalMaterialCost,
                    CAST(COALESCE(SUM(pl.hourly_rate * pl.estimated_hours), 0) AS DECIMAL(19,2)) AS totalLaborCost
                FROM projects p
                LEFT JOIN project_materials pm ON p.id = pm.project_id
                LEFT JOIN project_labor pl ON p.id = pl.project_id
                WHERE p.id = :projectId
            """, nativeQuery = true)
    CostBreakdown getCostBreakdown(@Param("projectId") Long projectId);


}
