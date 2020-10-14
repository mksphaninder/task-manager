package com.krishDev.Tasks.repositories;

import java.util.Optional;

import com.krishDev.Tasks.models.TaskType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long> {

    Optional<TaskType> findByTaskStage(String taskStage);
}