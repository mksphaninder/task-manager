package com.krishDev.Tasks.repositories;

import java.util.List;
import java.util.Optional;

import com.krishDev.Tasks.models.Task;
import com.krishDev.Tasks.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    public List<Task> findByUser(User user);
}