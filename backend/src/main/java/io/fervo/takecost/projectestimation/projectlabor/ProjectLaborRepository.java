package io.fervo.takecost.projectestimation.projectlabor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectLaborRepository extends JpaRepository<ProjectLabor, Long> {
    Page<ProjectLabor> findAllByProjectId(Long projectId, Pageable pageable);
}
