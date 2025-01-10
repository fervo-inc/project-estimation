package io.fervo.takecost.projectestimation.dto;

import io.fervo.takecost.projectestimation.model.Project;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record ProjectDTO(
        UUID id,
        @NotBlank String name,
        String description,
        @NotBlank String location,
        @NotNull LocalDate startDate,
        LocalDate endDate
) {
    public static ProjectDTO fromProject(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getLocation(),
                project.getStartDate(),
                project.getEndDate());
    }

    public Project toProject() {
        return new Project(this.id, this.name, this.description, this.location, this.startDate, this.endDate);
    }
}