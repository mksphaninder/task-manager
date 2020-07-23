package com.krishDev.Tasks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.krishDev.Tasks.controllers.TaskController;
import com.krishDev.Tasks.models.Task;
import com.krishDev.Tasks.models.TaskType;
import com.krishDev.Tasks.models.User;
import com.krishDev.Tasks.repositories.TaskRepository;
import com.krishDev.Tasks.repositories.UserRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.JsonPathAssertions;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    private static final ObjectMapper om = new ObjectMapper();
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private UserRepository userRepository;

    User user = new User(1L, "TestUser", "test@test.com", new Date(), "test");
    Task task1 = new Task(1L, "task1", "taskDesc1", new Date(), false, user, new TaskType(1L, "Backlog"));
    Task task2 = new Task(2L, "task2", "taskDesc2", new Date(), false, user, new TaskType(2L, "Production"));
    Task postTask = new Task(3L, "task3", "taskDesc3", new Date(), false, null, new TaskType(2L, null));

    @Before
    public void initGetTasks() {
        User user = new User();
        Task task = new Task();
        List<Task> tasks = List.of(task);
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(taskRepository.findByUser(user)).thenReturn(tasks);
    }
    @Test
    public void getTasks() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.get("/users/" + Mockito.anyLong() + "/tasks"))
        .andExpect(status().isOk());
    }
    @Before
    public void intTaskUserNotFound() {
        Mockito.when(userRepository.findById(Mockito.isNull())).thenReturn(Optional.empty());
    }
    @Test
    public void getTasksUserNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users//tasks"))
        .andExpect(status().isNotFound());
    }
    @Before
    public void initGetSingleTask() {
        Optional<User> optUser = Optional.ofNullable(user);
        // List<Task> tasks = List.of(task);
        Mockito.when(userRepository.findById(1L)).thenReturn(optUser);
        Mockito.when(taskRepository.findByUser(user)).thenReturn(List.of(task1, task2));
    }
    @Test
    public void GetSingleTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/tasks/1"))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.taskTitle", is("task1")));
    }

    @Before
    public void initAddTask() {
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(new Task());
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
    }

    @Test
    public void Addtask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/1/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(postTask)))
        .andExpect(status().isCreated());
    }

    @Before
    public void initEditTask() {
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(taskRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(task1));
        Mockito.when(taskRepository.save(Mockito.any())).thenReturn(task1);
    }

    @Test
    public void editTask() throws Exception{
        mockMvc
        .perform(MockMvcRequestBuilders
        .put("/users/1/tasks/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(om.writeValueAsString(postTask)))
        .andExpect(status().isCreated());

        mockMvc
        .perform(MockMvcRequestBuilders
        .put("/users/1/tasks/1"))
        .andExpect(status().isBadRequest());
    }

    @Test
    public void DeleteTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
        .delete("/users/1/tasks/1"))
        .andExpect(status().isOk());
    }
}