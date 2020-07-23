package com.krishDev.Tasks.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class TaskType {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String taskStage;

    @OneToMany(mappedBy = "taskType", cascade = CascadeType.ALL)
    List<Task> tasks = new ArrayList<>();

    public TaskType() {}

    public TaskType(Long id, String taskStage) {
        this.id = id;
        this.taskStage = taskStage;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskStage() {
        return this.taskStage;
    }

    public void setTaskStage(String taskStage) {
        this.taskStage = taskStage;
    }

    public void addTask(Task task){
        task.setTaskType(this);
        tasks.add(task);
    }

    public void removeTask(Task task) {
        task.setTaskType(null);
        tasks.remove(task);
    }
}