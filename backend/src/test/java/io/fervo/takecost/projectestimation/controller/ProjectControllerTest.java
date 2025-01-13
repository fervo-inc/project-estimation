package io.fervo.takecost.projectestimation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.fervo.takecost.projectestimation.config.OpenApiConfig;
import io.fervo.takecost.projectestimation.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProjectController.class)
@Disabled
class ProjectControllerTest {

    private static final String BASE_URL = "/api/v1/projects"; // Extracted URL constant
    private static final String MOCK_TOKEN = "mock-jwt-token";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProjectService projectService;

    @MockitoBean
    private OpenApiConfig.JwtUtils jwtUtils;


    @BeforeEach
    void setUp() {
        Mockito.when(jwtUtils.validateToken(eq(MOCK_TOKEN), any())).thenReturn(true);
        Mockito.when(jwtUtils.extractUsername(MOCK_TOKEN)).thenReturn("admin");
        Mockito.when(jwtUtils.extractRoles(MOCK_TOKEN)).thenReturn(List.of("ROLE_ADMIN"));
    }

    @Test
    void testCreateProject() throws Exception {
        ProjectDTO project = createProjectDTO(null, "New Project");
        ProjectDTO savedProject = createProjectDTO(UUID.randomUUID(), "New Project");

        Mockito.when(projectService.createProject(any(ProjectDTO.class))).thenReturn(savedProject.toProject());

        mockMvc.perform(post(BASE_URL)
                        .header("Authorization", "Bearer " + MOCK_TOKEN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("New Project"));
    }

    @Test
    void testCreateProjectWithDuplicateName() throws Exception {
        ProjectDTO project = createProjectDTO(null, "Duplicate Project");

        Mockito.when(projectService.createProject(any(ProjectDTO.class)))
                .thenThrow(new IllegalArgumentException("Project with the same name already exists"));

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(project)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Project with the same name already exists"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"));
    }

    @Test
    void testGetAllProjects() throws Exception {
        ProjectDTO project = createProjectDTO(UUID.randomUUID(), "Project 1");
        Page<ProjectDTO> projectPage = new PageImpl<>(List.of(project), PageRequest.of(0, 10), 1);

        Mockito.when(projectService.getAllProjects(any(PageRequest.class))).thenReturn(projectPage.map(ProjectDTO::toProject));

        mockMvc.perform(get(BASE_URL)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Project 1"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void testUpdateProject() throws Exception {
        UUID projectId = UUID.randomUUID();
        ProjectDTO updatedProject = createProjectDTO(projectId, "Updated Project");

        Mockito.when(projectService.updateProject(eq(projectId), any(ProjectDTO.class))).thenReturn(updatedProject.toProject());

        mockMvc.perform(put(BASE_URL + "/" + projectId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedProject)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Project"));
    }

    @Test
    void testDeleteProject() throws Exception {
        UUID projectId = UUID.randomUUID();

        Mockito.doNothing().when(projectService).deleteProject(projectId);

        mockMvc.perform(delete(BASE_URL + "/" + projectId))
                .andExpect(status().isOk())
                .andExpect(content().string("Project deleted successfully"));
    }

    // Extracted helper method to create reusable ProjectDTO instances
    private ProjectDTO createProjectDTO(UUID id, String name) {
        return new ProjectDTO(
                id,
                name,
                "Description",
                "Location",
                LocalDate.parse("2025-01-01"),
                LocalDate.parse("2025-12-31")
        );
    }
}