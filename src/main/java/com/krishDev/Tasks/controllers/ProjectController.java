package com.krishDev.Tasks.controllers;

import java.util.List;

import com.krishDev.Tasks.models.Project;
import com.krishDev.Tasks.models.Task;
import com.krishDev.Tasks.repositories.ProjectRepository;
import com.krishDev.Tasks.repositories.TaskRepository;
import com.krishDev.Tasks.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Get all projects for the User
     * @param userId
     */
    @GetMapping("/users/{userId}/projects/")
    public List<Project> showProjects(@PathVariable Long userId) {
        
    }

    /**
     * Add new project for the user.
     * @param userId
     * @return Project
     */
    @PostMapping("/users/{userId}/projects")
    public ResponseEntity<Object> addProject(@PathVariable Long userId) {

    }

    /**
     * Edit a project
     * @param userId
     * @param projectId
     * @return
     */
    @PutMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<Object> editProject(@PathVariable Long userId, @PathVariable Long projectId) {

    }

    /**
     * Delete a project and all the related Tasks
     * @param userId
     * @param projectId
     * @return
     */
    @DeleteMapping("/users/{userId}/projects/{projectId}")
    public ResponseEntity<Object> editProject(@PathVariable Long userId, @PathVariable Long projectId) {

    }

    /**
     * Show tasks related to single project
     * @param userId
     * @param projectId
     * @return List of Tasks
     */
    @GetMapping("/users/{userId}/projects/{projectId}/tasks")
    public List<Task> showAllTasksForProject(@PathVariable Long userId, @PathVariable Long projectId) {

    }
    /**
     * Add a task to the project
     * @param userId
     * @param projectId
     * @return
     */
    @PostMapping("/users/{userId}/projects/{projectId}/tasks")
    public ResponseEntity<Object> AddTaskForProject(@PathVariable Long userId, @PathVariable Long projectId) {

    }
    
}