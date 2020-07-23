package com.krishDev.Tasks.services;

import java.util.List;
import java.util.Optional;

import com.krishDev.Tasks.models.User;
import com.krishDev.Tasks.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    public boolean userExists(String email) {
        User user = userRepository.findByEmail(email);
    
        return user == null ? false: true;
    }
}