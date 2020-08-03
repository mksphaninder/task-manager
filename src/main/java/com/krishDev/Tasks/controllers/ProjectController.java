package com.krishDev.Tasks.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.krishDev.Tasks.models.Project;
import com.krishDev.Tasks.models.Task;
import com.krishDev.Tasks.models.TaskType;
import com.krishDev.Tasks.models.User;
import com.krishDev.Tasks.repositories.ProjectRepository;
import com.krishDev.Tasks.repositories.TaskRepository;
import com.krishDev.Tasks.repositories.TaskTypeRepository;
import com.krishDev.Tasks.repositories.UserRepository;

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
            String errors = result.getFieldErrors().stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.toList()).toString();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
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
            String errors = result.getFieldErrors().stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.toList()).toString();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
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

        if (!dbProject.get().getUser().equals(user.get())) {
            return new ResponseEntity<Object>("Not Authorized", HttpStatus.UNAUTHORIZED);
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
        Optional<User> user = userRepository.findById(userId);
        Optional<Project> project = projectRepository.findById(projectId);
        if (user.get().equals(project.get().getUser())) {
            projectRepository.deleteById(projectId);
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not Authorized");
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
        if (project.isPresent()) {
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
            String errors = result.getFieldErrors().stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.toList()).toString();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<User> user = userRepository.findById(userId);
        if (project.isPresent()) {
            System.out.println(user.get());
            System.out.println(project.get().getUser());
            System.out.println(user.get().equals(project.get().getUser()));
            if (user.get().equals(project.get().getUser())) {
                task.setProject(project.get());
                String taskStage = task.getTaskType().getTaskStage();
                Optional<Object> taskType = taskTypeRepository.findByTaskStage(taskStage);

                if (!taskType.isPresent()) {
                    TaskType savedTaskType = taskTypeRepository.save(task.getTaskType());
                    taskType = Optional.ofNullable(savedTaskType);
                }
                task.setTaskType((TaskType) taskType.get());
                Task savedTask = taskRepository.save(task);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                        .buildAndExpand(savedTask.getId()).toUri();
                return ResponseEntity.created(location).body(savedTask);

            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not Authorized");
            }
        }
        return new ResponseEntity<>("Project does not exist", HttpStatus.NOT_FOUND);
    }

    @PutMapping("/users/{userId}/projects/{projectId}/tasks/{taskId}")
    public ResponseEntity<Object> editTask(@PathVariable Long userId, @PathVariable Long projectId,
            @PathVariable Long taskId, @Valid @RequestBody Task task, BindingResult result) {

        if (result.hasErrors()) {
            String errors = result.getFieldErrors().stream().map(x -> x.getDefaultMessage())
                    .collect(Collectors.toList()).toString();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errors);
        }
        Optional<Project> project = projectRepository.findById(projectId);
        Optional<User> user = userRepository.findById(userId);
        Optional<Task> dbTask = taskRepository.findById(taskId);
        if (project.isPresent() && dbTask.isPresent()) {
            if (user.get().equals(project.get().getUser())) {
                if (dbTask.isPresent()) {
                    task.setId(dbTask.get().getId());
                }
                task.setProject(project.get());
                String taskStage = task.getTaskType().getTaskStage();
                Optional<Object> taskType = taskTypeRepository.findByTaskStage(taskStage);

                if (!taskType.isPresent()) {
                    TaskType savedTaskType = taskTypeRepository.save(task.getTaskType());
                    taskType = Optional.ofNullable(savedTaskType);
                }
                task.setTaskType((TaskType) taskType.get());
                Task savedTask = taskRepository.save(task);
                URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                        .buildAndExpand(savedTask.getId()).toUri();
                return dbTask.isPresent() ? new ResponseEntity<>(HttpStatus.OK)
                        : ResponseEntity.created(location).body(savedTask);

            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not Authorized");
            }
        }
        return new ResponseEntity<>("Project does not exist", HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/users/{userId}/projects/{projectId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long userId, @PathVariable Long projectId, @PathVariable Long taskId) {

        Optional<Project> project = projectRepository.findById(projectId);
        Optional<User> user = userRepository.findById(userId);

        if (project.isPresent() && user.isPresent()) {
            if(user.get().equals(project.get().getUser())){
                Optional<Task> task = taskRepository.findById(taskId);
                if (task.isPresent()) {
                    taskRepository.delete(task.get());
                }
            }else{
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }
    }

}