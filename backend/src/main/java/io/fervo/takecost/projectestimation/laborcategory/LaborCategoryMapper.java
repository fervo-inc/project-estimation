package io.fervo.takecost.projectestimation.laborcategory;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LaborCategoryMapper {
    LaborCategoryDTO toDTO(LaborCategory laborCategory);

    LaborCategory toEntity(LaborCategoryDTO laborCategoryDTO);
}
