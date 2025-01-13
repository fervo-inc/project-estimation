package io.fervo.takecost.projectestimation.projectlabor;

import io.fervo.takecost.projectestimation.laborcategory.LaborCategory;
import io.fervo.takecost.projectestimation.project.model.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "project_labor")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectLabor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "fk_project_labor_project"))
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "labor_category_id", foreignKey = @ForeignKey(name = "fk_project_labor_category"))
    private LaborCategory laborCategory;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal estimatedHours;
}
