package com.example.jakoo.entity;

public class TaskTabItem {
    private Task task;
    private String tags;
    private String priorityValue;
    private String statusValue;

    public TaskTabItem(Task task, String tags, String priorityValue, String statusValue) {
        this.task = task;
        this.tags = tags;
        this.priorityValue = priorityValue;
        this.statusValue = statusValue;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getPriorityValue() {
        return priorityValue;
    }

    public void setPriorityValue(String priorityValue) {
        this.priorityValue = priorityValue;
    }

    public String getStatusValue() {
        return statusValue;
    }

    public void setStatusValue(String statusValue) {
        this.statusValue = statusValue;
    }
}
