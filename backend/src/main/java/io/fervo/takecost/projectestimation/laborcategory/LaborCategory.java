package io.fervo.takecost.projectestimation.laborcategory;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "labor_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaborCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column
    private String description;
}
