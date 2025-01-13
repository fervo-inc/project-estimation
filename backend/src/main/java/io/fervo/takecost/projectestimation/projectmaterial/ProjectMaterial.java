package io.fervo.takecost.projectestimation.projectmaterial;

import io.fervo.takecost.projectestimation.material.MaterialCatalog;
import io.fervo.takecost.projectestimation.project.model.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "project_materials")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMaterial {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "material_catalog_id")
    private MaterialCatalog materialCatalog;
    @Column(nullable = false)
    private Double quantity;
    @Column(nullable = false)
    private BigDecimal unitPrice;
    @Column(length = 500)
    private String notes;
}
