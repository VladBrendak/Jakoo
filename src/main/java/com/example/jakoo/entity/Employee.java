package com.example.jakoo.entity;

public class Employee {
    private Long employeeId;
    private String name;
    private String description;
    private Boolean  isChecked;

    public Employee() {}

    public Employee(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }

    @Override
    public String toString() {
        return "Employee{ " + name + " }";
    }
}
