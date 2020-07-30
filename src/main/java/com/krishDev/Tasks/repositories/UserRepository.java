package com.krishDev.Tasks.repositories;

import com.krishDev.Tasks.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    
    public User findByEmail(String email);

}