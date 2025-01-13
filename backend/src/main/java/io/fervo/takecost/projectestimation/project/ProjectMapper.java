package io.fervo.takecost.projectestimation.project;

import io.fervo.takecost.projectestimation.project.model.Project;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectDTO toDTO(Project project);

    Project toEntity(ProjectDTO projectDTO);
}
