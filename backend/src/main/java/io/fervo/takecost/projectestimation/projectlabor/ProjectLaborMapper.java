package io.fervo.takecost.projectestimation.projectlabor;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectLaborMapper {
    ProjectLaborDTO toDTO(ProjectLabor projectLabor);

    ProjectLabor toEntity(ProjectLaborDTO projectLaborDTO);
}
