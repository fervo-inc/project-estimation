package io.fervo.takecost.projectestimation.service;

import io.fervo.takecost.projectestimation.dto.ProjectDTO;
import io.fervo.takecost.projectestimation.model.Project;
import io.fervo.takecost.projectestimation.repository.ProjectRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    ProjectServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject() {
        var savedProject = new Project(UUID.randomUUID(), "New Project", "Description", "Location", LocalDate.parse("2025-01-01"), LocalDate.parse("2025-12-31"));

        when(projectRepository.save(any(Project.class))).thenReturn(savedProject);

        var projectDTO = new ProjectDTO(null, "New Project", "Description", "Location", LocalDate.now(), LocalDate.now().plusMonths(11));
        var result = ProjectDTO.fromProject(projectService.createProject(projectDTO));

        assertNotNull(result.id());
        assertEquals("New Project", result.name());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testCreateProjectWithDuplicateName_Service() {
        var projectDTO = new ProjectDTO(null, "Duplicate Project", "Description", "Location", LocalDate.now(), LocalDate.now().plusMonths(11));

        Mockito.when(projectRepository.findByName("Duplicate Project"))
                .thenReturn(Optional.of(new Project()));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            projectService.createProject(projectDTO);
        });

        assertEquals("Project with the same name already exists", exception.getMessage());
        Mockito.verify(projectRepository, Mockito.times(1)).findByName("Duplicate Project");
    }

    @Test
    void testGetAllProjects() {
        Project project = new Project(UUID.randomUUID(), "Project 1", "Description", "Location", LocalDate.now(), LocalDate.now().plusMonths(11));
        Page<Project> page = new PageImpl<>(List.of(project), PageRequest.of(0, 10), 1);

        when(projectRepository.findAll(any(PageRequest.class))).thenReturn(page);

        Page<Project> result = projectService.getAllProjects(PageRequest.of(0, 10));

        assertEquals(1, result.getTotalElements());
        assertEquals("Project 1", result.getContent().get(0).getName());
    }

    @Test
    void testUpdateProject() {
        UUID projectId = UUID.randomUUID();
        Project project = new Project(projectId, "Old Project", "Old Description", "Old Location", LocalDate.now(), LocalDate.now().plusMonths(11));
        Project updatedProject = new Project(projectId, "Updated Project", "Updated Description", "Updated Location", LocalDate.now(), LocalDate.now().plusMonths(11));

        when(projectRepository.findById(projectId)).thenReturn(Optional.of(project));
        when(projectRepository.save(any(Project.class))).thenReturn(updatedProject);

        ProjectDTO projectDTO = new ProjectDTO(projectId, "Updated Project", "Updated Description", "Updated Location", LocalDate.now(), LocalDate.now().plusMonths(11));
        ProjectDTO result = ProjectDTO.fromProject(projectService.updateProject(projectId, projectDTO));

        assertEquals("Updated Project", result.name());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void testDeleteProject() {
        UUID projectId = UUID.randomUUID();

        when(projectRepository.existsById(projectId)).thenReturn(true);
        doNothing().when(projectRepository).deleteById(projectId);

        projectService.deleteProject(projectId);

        verify(projectRepository, times(1)).deleteById(projectId);
    }
}
