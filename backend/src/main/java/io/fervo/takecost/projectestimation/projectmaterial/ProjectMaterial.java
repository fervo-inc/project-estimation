package io.fervo.takecost.projectestimation.projectmaterial;

import io.fervo.takecost.projectestimation.material.MaterialCatalog;
import io.fervo.takecost.projectestimation.project.model.Project;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "project_materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "material_catalog_id")
    private MaterialCatalog materialCatalog;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(length = 500)
    private String notes;
}
