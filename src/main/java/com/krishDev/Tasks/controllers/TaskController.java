package com.krishDev.Tasks.controllers;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.krishDev.Tasks.models.Task;
import com.krishDev.Tasks.models.User;
import com.krishDev.Tasks.repositories.TaskRepository;
import com.krishDev.Tasks.repositories.TaskTypeRepository;
import com.krishDev.Tasks.repositories.UserRepository;

import org.apache.catalina.connector.Response;
import org.hibernate.annotations.NotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskTypeRepository taskTypeRepository;

    @GetMapping("/users/{id}/tasks")
    public List<Task> getTasksForUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        List<Task> userTasks = taskRepository.findByUser(user.get());
        return userTasks;
    }

    @PostMapping("/users/{id}/tasks")
    public ResponseEntity<Object> addTask(@PathVariable Long id, @Valid @RequestBody Task task) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()) {
            task.setUser(user.get());
            task.setTaskType(taskTypeRepository.findById(task.getTaskType().getId()).get());
            Task savedTask = taskRepository.save(task);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedTask.getId())
                .toUri();
            return ResponseEntity.created(location).body(savedTask);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/users/{userId}/tasks/{taskId}")
    public ResponseEntity<Object> getTaskById(@PathVariable Long userId, @PathVariable Long taskId) {
        Optional<User> user = userRepository.findById(userId);
        List<Task> userTasks = taskRepository.findByUser(user.get());
        if(userTasks.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Task> requiredTask = userTasks.stream().filter(t -> {
            return t.getId() == taskId;
        }).collect(Collectors.toList());

        if(requiredTask.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(requiredTask.get(0), HttpStatus.OK);
    }

    @PutMapping("/users/{userId}/tasks/{taskId}")
    public ResponseEntity<Object> editTask(@PathVariable Long userId, @PathVariable Long taskId, @Valid @RequestBody Task task, BindingResult bindingResult) {
        if(userId == null || taskId == null || bindingResult.hasErrors()) {
            new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<Task> requiredTask = taskRepository.findById(taskId);

        if(!requiredTask.isPresent()) {
            task.setUser(user.get());
            Task savedTask = taskRepository.save(task);
            
            URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedTask.getId())
            .toUri();

            return ResponseEntity.created(location).body(savedTask);
        }
        requiredTask.get().setTaskTitle(task.getTaskTitle());
        requiredTask.get().setTaskDesc(task.getTaskDesc());
        requiredTask.get().setDueDate(task.getDueDate());
        requiredTask.get().setDone(task.getDone());
        Task savedTask = taskRepository.save(requiredTask.get());
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedTask.getId())
            .toUri();
        savedTask.setUser(null);
        return ResponseEntity.created(location).body(savedTask);
    }

    @DeleteMapping("/users/{userId}/tasks/{taskId}")
    public void deleteTask(@PathVariable Long userId, @PathVariable Long taskId) throws NotFoundException {
        Optional<User> user = userRepository.findById(userId);

        if(!user.isPresent()) {
            throw new NotFoundException();
        }
        
        List<Task> userTasks = taskRepository.findByUser(user.get());

        List<Task> requiredTasks = userTasks
            .stream()
            .filter(t -> {
                return t.getId() == taskId;
            }).collect(Collectors.toList());

        if(requiredTasks.isEmpty()) {
            throw new NotFoundException();
        }
        
        Task requiredTask = requiredTasks.get(0);
        
        taskRepository.delete(requiredTask);
    }

}