package io.fervo.takecost.projectestimation.projectmaterial;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMaterialMapper {
    ProjectMaterialDTO toDTO(ProjectMaterial projectMaterial);

    ProjectMaterial toEntity(ProjectMaterialDTO projectMaterialDTO);
}
