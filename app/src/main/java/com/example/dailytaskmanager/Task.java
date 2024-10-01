package com.example.dailytaskmanager;

public class Task {
    private String taskName;
    private String taskTime;

    public Task(String taskName, String taskTime) {
        this.taskName = taskName;
        this.taskTime = taskTime;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }
}
