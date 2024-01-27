package com.example.jakoo.entity;

public class EmployeeGroup {

    public EmployeeGroup() {}

    public EmployeeGroup(String name) {
        this.name = name;
    }
    private Long groupId;
    private String name;

    public Long getGroupId() {
        return groupId;
    }
    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}

