package com.krishDev.Tasks.controllers;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.krishDev.Tasks.models.User;
import com.krishDev.Tasks.repositories.UserRepository;
import com.krishDev.Tasks.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/users")
    public ResponseEntity<Object> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            System.out.println(bindingResult.hasErrors());
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }
        if(userService.userExists(user.getEmail())) {
            return ResponseEntity.status(409).body("User already registered with the email "+ user.getEmail());
        }
        User savedUser = userRepository.save(user);
    
        if(savedUser != null) {
            URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(savedUser.getId())
            .toUri();

            return ResponseEntity.created(location).build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.get(); 
    }
}