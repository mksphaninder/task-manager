package com.krishDev.Tasks.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
public class Task {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String taskTitle;

    private String taskDesc;

    @Temporal(TemporalType.DATE)
    private Date dueDate;

    private Boolean done;

    @ManyToOne() @NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name="user_id")
    @JsonIgnore
    private User user;

    @ManyToOne
    private TaskType taskType;

    public Task() {
    }

    public Task(Long id, String taskTitle, String taskDesc, Date dueDate, Boolean done, User user, TaskType taskType) {
        this.id = id;
        this.taskTitle = taskTitle;
        this.taskDesc = taskDesc;
        this.dueDate = dueDate;
        this.done = done;
        this.user = user;
        this.taskType = taskType;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskTitle() {
        return this.taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDesc() {
        return this.taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Boolean isDone() {
        return this.done;
    }

    public Boolean getDone() {
        return this.done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public TaskType getTaskType() {
        return this.taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", taskTitle='" + getTaskTitle() + "'" +
            ", taskDesc='" + getTaskDesc() + "'" +
            ", dueDate='" + getDueDate() + "'" +
            ", done='" + isDone() + "'" +
            ", user='" + getUser() + "'" +
            ", taskType='" + getTaskType() + "'" +
            "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Task)) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equals(id, task.id) && Objects.equals(taskTitle, task.taskTitle) && Objects.equals(taskDesc, task.taskDesc) && Objects.equals(dueDate, task.dueDate) && Objects.equals(done, task.done) && Objects.equals(user, task.user) && Objects.equals(taskType, task.taskType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, taskTitle, taskDesc, dueDate, done, user, taskType);
    }

}