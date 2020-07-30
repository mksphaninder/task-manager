package com.krishDev.Tasks.services;

import java.util.List;

import com.krishDev.Tasks.repositories.TaskRepository;
import com.krishDev.Tasks.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;
    
}