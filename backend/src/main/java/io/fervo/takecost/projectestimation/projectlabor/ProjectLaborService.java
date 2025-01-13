package io.fervo.takecost.projectestimation.projectlabor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectLaborService {
    private final ProjectLaborRepository repository;

    public ProjectLabor save(ProjectLabor projectLabor) {
        return repository.save(projectLabor);
    }

    public ProjectLabor getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project Labor not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<ProjectLabor> getAllByProjectId(Long projectId, Pageable pageable) {
        return repository.findAllByProjectId(projectId, pageable);
    }
}
