package io.fervo.takecost.projectestimation.projectmaterial;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectMaterialMapper {
    ProjectMaterialMapper INSTANCE = Mappers.getMapper(ProjectMaterialMapper.class);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "materialCatalog.id", target = "materialId")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "notes", target = "notes")
    ProjectMaterialDTO toDTO(ProjectMaterial projectMaterial);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "materialId", target = "materialCatalog.id")
    @Mapping(source = "quantity", target = "quantity")
    @Mapping(source = "unitPrice", target = "unitPrice")
    @Mapping(source = "notes", target = "notes")
    ProjectMaterial toEntity(ProjectMaterialDTO projectMaterialDTO);
}
