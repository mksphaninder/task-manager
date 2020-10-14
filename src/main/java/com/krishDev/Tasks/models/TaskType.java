package com.krishDev.Tasks.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class TaskType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String taskStage;
    // @JsonIgnore
    @JsonManagedReference
    @OneToMany(mappedBy = "taskType", fetch = FetchType.LAZY)
    private List<Task> tasks = new ArrayList<>();

    public TaskType() {
    }

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

    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task task) {
        task.setTaskType(this);
        tasks.add(task);
    }

    public void removeTask(Task task) {
        task.setTaskType(null);
        tasks.remove(task);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TaskType)) {
            return false;
        }
        TaskType taskType = (TaskType) o;
        // return Objects.equals(id, taskType.id) && Objects.equals(taskStage,
        // taskType.taskStage) && Objects.equals(tasks, taskType.tasks);
        return id != null && id.equals(taskType.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

}