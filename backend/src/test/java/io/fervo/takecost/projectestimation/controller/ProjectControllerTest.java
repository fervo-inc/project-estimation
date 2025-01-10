package io.fervo.takecost.projectestimation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fervo.takecost.projectestimation.dto.ProjectDTO;
import io.fervo.takecost.projectestimation.repository.ProjectRepository;
import io.fervo.takecost.projectestimation.service.ProjectService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private ProjectRepository projectRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateProject() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO(null, "New Project", "Description", "Location", "2025-01-01", "2025-12-31");
        ProjectDTO savedProjectDTO = new ProjectDTO(UUID.randomUUID(), "New Project", "Description", "Location", "2025-01-01", "2025-12-31");

        Mockito.when(projectService.createProject(any(ProjectDTO.class))).thenReturn(savedProjectDTO);

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    void testCreateProjectWithDuplicateName() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO(null, "Duplicate Project", "Description", "Location", "2025-01-01", "2025-12-31");

        Mockito.when(projectService.createProject(any(ProjectDTO.class)))
                .thenThrow(new IllegalArgumentException("Project with the same name already exists"));

        mockMvc.perform(post("/api/v1/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(projectDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Project with the same name already exists"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void testGetAllProjects() throws Exception {
        ProjectDTO projectDTO = new ProjectDTO(UUID.randomUUID(), "Project 1", "Description", "Location", "2025-01-01", "2025-12-31");
        Page<ProjectDTO> page = new PageImpl<>(List.of(projectDTO), PageRequest.of(0, 10), 1);

        Mockito.when(projectService.getAllProjects(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/projects")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Project 1"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testUpdateProject() throws Exception {
        UUID projectId = UUID.randomUUID();
        ProjectDTO updatedProjectDTO = new ProjectDTO(projectId, "Updated Project", "Updated Description", "Updated Location", "2025-01-01", "2025-12-31");

        Mockito.when(projectService.updateProject(eq(projectId), any(ProjectDTO.class))).thenReturn(updatedProjectDTO);

        mockMvc.perform(put("/api/v1/projects/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProjectDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"));
    }

    @Test
    void testDeleteProject() throws Exception {
        UUID projectId = UUID.randomUUID();

        Mockito.doNothing().when(projectService).deleteProject(projectId);

        mockMvc.perform(delete("/api/v1/projects/" + projectId))
                .andExpect(status().isOk())
                .andExpect(content().string("Project deleted successfully"));
    }
}
