package io.fervo.takecost.projectestimation.laborcategory;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LaborCategoryMapper {
    LaborCategoryMapper INSTANCE = Mappers.getMapper(LaborCategoryMapper.class);

    LaborCategoryDTO toDTO(LaborCategory laborCategory);

    LaborCategory toEntity(LaborCategoryDTO laborCategoryDTO);
}
