package io.fervo.takecost.projectestimation.projectmaterial;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectMaterialService {
    private final ProjectMaterialRepository repository;

    public ProjectMaterial save(ProjectMaterial projectMaterial) {
        return repository.save(projectMaterial);
    }

    public ProjectMaterial getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Project Material not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<ProjectMaterial> getAllByProjectId(Long projectId, Pageable pageable) {
        return repository.findAllByProjectId(projectId, pageable);
    }
}
