package io.fervo.takecost.projectestimation.projectlabor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectLaborRepository extends JpaRepository<ProjectLabor, Long> {
    @EntityGraph(attributePaths = {"laborCategory", "project"})
    Page<ProjectLabor> findAllByProjectId(Long projectId, Pageable pageable);
}
