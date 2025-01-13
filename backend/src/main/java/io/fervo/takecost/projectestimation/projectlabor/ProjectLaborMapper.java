package io.fervo.takecost.projectestimation.projectlabor;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectLaborMapper {
    ProjectLaborMapper INSTANCE = Mappers.getMapper(ProjectLaborMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "laborCategory.id", target = "laborCategoryId")
    @Mapping(source = "hourlyRate", target = "hourlyRate")
    @Mapping(source = "estimatedHours", target = "estimatedHours")
    @Mapping(source = "laborCategory", target = "laborCategory")
    ProjectLaborDTO toDTO(ProjectLabor projectLabor);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "laborCategoryId", target = "laborCategory.id")
    @Mapping(source = "hourlyRate", target = "hourlyRate")
    @Mapping(source = "estimatedHours", target = "estimatedHours")
    @Mapping(source = "laborCategory", target = "laborCategory")
    ProjectLabor toEntity(ProjectLaborDTO projectLaborDTO);
}
