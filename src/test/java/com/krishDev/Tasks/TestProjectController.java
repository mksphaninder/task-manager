package com.krishDev.Tasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Option;
import com.krishDev.Tasks.controllers.ProjectController;
import com.krishDev.Tasks.models.Project;
import com.krishDev.Tasks.models.Task;
import com.krishDev.Tasks.models.TaskType;
import com.krishDev.Tasks.models.User;
import com.krishDev.Tasks.repositories.ProjectRepository;
import com.krishDev.Tasks.repositories.TaskRepository;
import com.krishDev.Tasks.repositories.TaskTypeRepository;
import com.krishDev.Tasks.repositories.UserRepository;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Mockito.*;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class TestProjectController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskTypeRepository taskTypeRepository;

    User user = new User(1L, "testUser", "test@test.com", new Date(), "test");
    Project project = new Project(1L, "test Project", user);
    Project project2 = new Project(2L, null, user);
    List<Project> projects = List.of(project);
    Task task = new Task(1L, "taskTitle", "taskDesc", new Date(), true, new TaskType(1L, "taskStage"), project);

    @Test
    public void testShowProjects() throws Exception {
        // when
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));

        Mockito.when(projectRepository.findByUserId(Mockito.anyLong())).thenReturn(projects);

        // perform

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + 1 + "/projects")).andExpect(status().isOk());

    }

    @Test
    public void addProjectTest() throws Exception {
        // when
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(projectRepository.save(Mockito.any())).thenReturn(project);

        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/projects").accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void addProjectInvalidData() throws JsonProcessingException, Exception {
        // when
        // Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        // Mockito.when(projectRepository.save(Mockito.any())).thenReturn(project);
        
        mockMvc
            .perform(MockMvcRequestBuilders.post("/users/1/projects").accept(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(project2))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test 
    public void editProject() throws JsonProcessingException, Exception {
    // when
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(project));
        Mockito.when(projectRepository.save(Mockito.any())).thenReturn(project);

        mockMvc
            .perform(MockMvcRequestBuilders
                .put(
                    "/users/1/projects/1")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(project))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test 
    public void getTasks() throws Exception {
    // when
        // Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(projectRepository.findById(Mockito.any())).thenReturn(Optional.of(project));
        Mockito.when(taskRepository.findByProject(Mockito.any()))
        .thenReturn(List.of(new Task(1L, "test task", "test description", new Date(), true, new TaskType(1L, "Backlog"), project)));

        mockMvc
            .perform(MockMvcRequestBuilders
                .get(
                    "/users/1/projects/1/tasks"))
            .andExpect(status().isOk());
    }
    
    @Test
    public void getTaskProjectNotFound() throws Exception {
        Mockito.when(taskRepository.findByProject(Mockito.any())).thenReturn(List.of());
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(null));

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/projects/1/tasks")).andExpect(status().isNotFound());
    }

    @Test
    public void addTask() throws JsonProcessingException, Exception {
        Mockito.when(projectRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(project));
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(task);
        Mockito.when(taskTypeRepository.findByTaskStage(Mockito.any())).thenReturn(Optional.ofNullable(new TaskType()));
        Mockito.when(taskTypeRepository.save(Mockito.any())).thenReturn(new TaskType());
        
        mockMvc
            .perform(MockMvcRequestBuilders
                .post(
                    "/users/1/projects/1/tasks")
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated());
    }

}