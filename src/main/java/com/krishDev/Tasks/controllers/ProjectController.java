package com.krishDev.Tasks.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.krishDev.Tasks.models.Project;
import com.krishDev.Tasks.models.Task;
import com.krishDev.Tasks.models.TaskType;
import com.krishDev.Tasks.models.User;
import com.krishDev.Tasks.repositories.ProjectRepository;
import com.krishDev.Tasks.repositories.TaskRepository;
import com.krishDev.Tasks.repositories.TaskTypeRepository;
import com.krishDev.Tasks.repositories.UserRepository;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class ProjectController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskTypeRepository taskTypeRepository;

    /**
     * Get all projects for the User
     * 
     * @param userId
     */
    @GetMapping("/users/{userId}/projects")
    public List<Project> showProjects(@PathVariable Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if (!userRepository.findById(userId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user is not found");
        }
        List<Project> projects = projectRepository.findByUserId(userId);

        return projects;
    }

    /**
     * Add new project for the user.
     * 
     * @param userId
     * @param project
     * @return
     */
    @PostMapping("/users/{userId}/projects")
    public ResponseEntity<Object> addProject(@PathVariable Long userId, @Valid @RequestBody Project project,
            BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getAllErrors().toString());
        }

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {
            project.setUser(user.get());
            Project savedProject = projectRepository.save(project);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(savedProject.getId()).toUri();
            return ResponseEntity.created(location).body(savedProject);
        }
        return new ResponseEntity<Object>("user is not found", HttpStatus.NOT_FOUND);
    }

    /**
     * Edit a project
     * 
     * @param userId
     * @param projectId
     * @return
     */
    @PutMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<Object> editProject(@PathVariable Long userId, @PathVariable Long projectId,
            @Valid @RequestBody Project project, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getAllErrors().toString());
        }

        Optional<User> user = userRepository.findById(userId);

        if (!user.isPresent()) {
            return new ResponseEntity<Object>("User not found", HttpStatus.NOT_FOUND);
        }

        Optional<Project> dbProject = projectRepository.findById(projectId);

        if (!dbProject.isPresent()) {
            project.setUser(user.get());
            Project savedProject = projectRepository.save(project);
            return ResponseEntity.ok().build();
        }
        dbProject.get().setProject(project.getProject());
        Project savedProject = projectRepository.save(dbProject.get());
        return ResponseEntity.ok().build();
    }

    /**
     * Delete a project and all the related Tasks
     * 
     * @param userId
     * @param projectId
     * @return
     */
    @DeleteMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<Object> editProject(@PathVariable Long userId, @PathVariable Long projectId) {
        projectRepository.deleteById(projectId);
        return ResponseEntity.ok().build();
    }

    /**
     * Show tasks related to single project
     * 
     * @param userId
     * @param projectId
     * @return List of Tasks
     */
    @GetMapping("/users/{userId}/projects/{projectId}/tasks")
    public List<Task> showAllTasksForProject(@PathVariable Long userId, @PathVariable Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if(project.isPresent()) {
            List<Task> projectTasks = taskRepository.findByProject(project.get());
            return projectTasks;
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "project does not exist");
    }

    /**
     * Add a task to the project
     * 
     * @param userId
     * @param projectId
     * @param task
     * @return
     */
    @PostMapping("/users/{userId}/projects/{projectId}/tasks")
    public ResponseEntity<Object> AddTaskForProject(@PathVariable Long userId, @PathVariable Long projectId,
            @Valid @RequestBody Task task, BindingResult result) {
        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getAllErrors().toString());
        }
        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isPresent()) {
            task.setProject(project.get());
            Optional<Object> taskType = taskTypeRepository.findByTaskStage(task.getTaskType().getTaskStage());

            if(!taskType.isPresent()){
                TaskType savedTaskType = taskTypeRepository.save(task.getTaskType());
                taskType = Optional.ofNullable(savedTaskType);
            }
            task.setTaskType((TaskType) taskType.get());
            Task savedTask = taskRepository.save(task);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                    .buildAndExpand(savedTask.getId()).toUri();
            return ResponseEntity.created(location).body(savedTask);
        }
        return new ResponseEntity<>("Project does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users/{userId}/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<Object> editTask(@PathVariable Long userId, @PathVariable Long projectId,
            @PathVariable Long taskId, @Valid @RequestBody Task task, BindingResult result) {

        if (result.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, result.getAllErrors().toString());
        }
        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isPresent()) {
            task.setProject(project.get());
            taskRepository.save(task);

            return ResponseEntity.ok().build();
        }
        return new ResponseEntity<>("Project does not exist", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users/{userId}/projects/{projectId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long taskId) {

        Optional<Project> project = projectRepository.findById(projectId);

        if (project.isPresent()) {
            Optional<Task> task = taskRepository.findById(taskId);
            if (task.isPresent()) {
                taskRepository.delete(task.get());
            }
        }
    }

}