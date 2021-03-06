package com.krishDev.Tasks.repositories;

import java.util.List;

import com.krishDev.Tasks.models.Project;
import com.krishDev.Tasks.models.Task;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{
    public List<Task> findByProject(Project project);
}