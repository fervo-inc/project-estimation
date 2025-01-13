package io.fervo.takecost.projectestimation.material;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MaterialCatalogService {
    private final MaterialCatalogRepository repository;

    public MaterialCatalog save(MaterialCatalog materialCatalog) {
        return repository.save(materialCatalog);
    }

    public MaterialCatalog getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Material not found"));
    }

    public void delete(Long id) {
        // TODO: Do not allow delete if material is being used in a project.
        repository.deleteById(id);
    }

    public Page<MaterialCatalog> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    // TODO: Add method searchMaterials
}
