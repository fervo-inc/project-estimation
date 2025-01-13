package io.fervo.takecost.projectestimation.laborcategory;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LaborCategoryService {
    private final LaborCategoryRepository repository;

    public LaborCategory save(LaborCategory laborCategory) {
        return repository.save(laborCategory);
    }

    public LaborCategory getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Labor Category not found"));
    }

    public void delete(Long id) {
        // TODO: Do not allow if already in use.
        repository.deleteById(id);
    }

    public Page<LaborCategory> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
