package io.fervo.takecost.projectestimation.project;

import io.fervo.takecost.projectestimation.project.model.Project;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectMapper INSTANCE = Mappers.getMapper(ProjectMapper.class);

    ProjectDTO toDTO(Project project);

    Project toEntity(ProjectDTO projectDTO);
}