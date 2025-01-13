package io.fervo.takecost.projectestimation.material;

import io.fervo.takecost.projectestimation.vendor.Vendor;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "material_catalogs")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaterialCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(length = 255)
    private String category;

    @Column(length = 255)
    private String subCategory;

    @Column(length = 50, nullable = false)
    private String unitType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    // TODO: Add packageType and packagePrice for bulk materials

    @Column(nullable = false)
    private Integer inStock = 0;

    @Column(nullable = false)
    private Integer leadTimeDays = 0;

    @ManyToOne
    @JoinColumn(name = "vendor_id")
    private Vendor vendor; // TODO: We could have more than one vendor for a given material.

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedAt;
}
