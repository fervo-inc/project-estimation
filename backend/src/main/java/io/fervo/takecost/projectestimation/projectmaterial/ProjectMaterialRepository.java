package io.fervo.takecost.projectestimation.projectmaterial;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectMaterialRepository extends JpaRepository<ProjectMaterial, Long> {
    Page<ProjectMaterial> findAllByProjectId(Long projectId, Pageable pageable);
}
