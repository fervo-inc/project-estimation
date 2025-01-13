package io.fervo.takecost.projectestimation.projectmaterial;

import io.fervo.takecost.projectestimation.material.MaterialCatalogRepository;
import io.fervo.takecost.projectestimation.project.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProjectMaterialService {
    private final ProjectMaterialRepository repository;
    private final ProjectRepository projectRepository;
    private final MaterialCatalogRepository materialCatalogRepository;

    public ProjectMaterial save(ProjectMaterial projectMaterial) {
        var project = projectRepository.findById(projectMaterial.getProject().getId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectMaterial.getProject().getId()));

        var materialCatalog = materialCatalogRepository.findById(projectMaterial.getMaterialCatalog().getId())
                .orElseThrow(() -> new EntityNotFoundException("MaterialCatalog not found with ID: " + projectMaterial.getMaterialCatalog().getId()));

        projectMaterial.setProject(project);
        projectMaterial.setMaterialCatalog(materialCatalog);

        return repository.save(projectMaterial);
    }

    public ProjectMaterial update(ProjectMaterial projectMaterial) {
        ProjectMaterial existingMaterial = getProjectMaterialById(projectMaterial.getId());
        updateDetails(existingMaterial, projectMaterial);
        return repository.save(existingMaterial);
    }

    private ProjectMaterial getProjectMaterialById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project Material not found with ID: " + id));
    }

    private void updateDetails(ProjectMaterial existingMaterial, ProjectMaterial updatedMaterial) {
        existingMaterial.setQuantity(updatedMaterial.getQuantity());
        existingMaterial.setUnitPrice(updatedMaterial.getUnitPrice());
        existingMaterial.setNotes(updatedMaterial.getNotes());
    }

    public ProjectMaterial getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Project Material not found"));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<ProjectMaterial> getAllByProjectId(Long projectId, Pageable pageable) {
        return repository.findAllByProjectId(projectId, pageable);
    }
}
