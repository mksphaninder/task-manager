package com.krishDev.Tasks.repositories;

import com.krishDev.Tasks.models.TaskType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long>{
    
}