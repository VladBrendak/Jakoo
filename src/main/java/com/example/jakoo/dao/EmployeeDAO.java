package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO {

    private final DatabaseConnector connector;

    public EmployeeDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<Employee> getEmployeesNotInGroup(Long groupId) {
        String sql = "SELECT * FROM Employee " +
                "WHERE NOT EXISTS (" +
                "    SELECT 1 FROM Members " +
                "    WHERE Members.employee = Employee.employee_id " +
                "    AND Members.employee_group = ?" +
                ");";
        List<Employee> employeeList = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, groupId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(resultSet.getLong("employee_id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setDescription(resultSet.getString("description"));
                    employeeList.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    public List<Employee> getEmployeesInGroup(Long groupId) {
        String sql = "SELECT Employee.* FROM Employee " +
                "JOIN Members ON Employee.employee_id = Members.employee " +
                "WHERE Members.employee_group = ?;";
        List<Employee> employeeList = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, groupId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(resultSet.getLong("employee_id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setDescription(resultSet.getString("description"));
                    employeeList.add(employee);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeList;
    }

    public List<Employee> getAllEmployees() {
        String sql = "SELECT * FROM Employee";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee e = new Employee();
                e.setEmployeeId(resultSet.getLong("employee_id"));
                e.setName(resultSet.getString("name"));
                e.setDescription(resultSet.getString("description"));
                employees.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Employee> getAllEmployeesWithChecked(long groupId) {
        String sql = "SELECT employee.employee_id, employee.name, " +
                "CASE " +
                "    WHEN employee.employee_id IN " +
                "        (SELECT members.employee FROM employee " +
                "        JOIN members ON employee.employee_id = members.employee " +
                "        WHERE members.employee_group = ?) " +
                "    THEN true " +
                "    ELSE false " +
                "END AS checked " +
                "FROM employee";

        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, groupId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Employee employee = new Employee();
                    employee.setEmployeeId(resultSet.getLong("employee_id"));
                    employee.setName(resultSet.getString("name"));
                    employee.setChecked(resultSet.getBoolean("checked"));
                    employees.add(employee);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public List<Employee> getAllEmployeesExcept(String employeeIdThatNotIncluded)
    {
        if (employeeIdThatNotIncluded.isBlank() || employeeIdThatNotIncluded.isEmpty())
        {
            return getAllEmployees();
        }

        String sql = "SELECT * FROM Employee WHERE employee_id NOT IN (" + employeeIdThatNotIncluded + ");";
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Employee e = new Employee();
                e.setEmployeeId(resultSet.getLong("employee_id"));
                e.setName(resultSet.getString("name"));
                e.setDescription(resultSet.getString("description"));
                employees.add(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    public void addEmployee(Employee employee) {
        String sql = "INSERT INTO Employee (name, description) VALUES (?, ?)";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getDescription());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editEmployee(Employee employee) {
        String sql = "UPDATE Employee SET name = ?, description = ? WHERE employee_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getDescription());
            preparedStatement.setLong(3, employee.getEmployeeId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteEmployee(Long emloyeeId) {
        String sql = "DELETE FROM Employee WHERE employee_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, emloyeeId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

