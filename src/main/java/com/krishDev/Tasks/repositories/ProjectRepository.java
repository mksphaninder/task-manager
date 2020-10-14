package com.krishDev.Tasks.repositories;

import java.util.List;

import com.krishDev.Tasks.models.Project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    public List<Project> findByUserId(Long userId);

}