package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.EmployeeGroup;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EmployeeGroupDAO {

    private final DatabaseConnector connector;

    public EmployeeGroupDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<EmployeeGroup> getAllEmployeeGroups() {
        String sql = "SELECT * FROM EmployeeGroup WHERE is_custom = true";
        List<EmployeeGroup> employeeGroups = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                EmployeeGroup group = new EmployeeGroup();
                group.setGroupId(resultSet.getLong("group_id"));
                group.setName(resultSet.getString("name"));
                employeeGroups.add(group);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employeeGroups;
    }

    public void addEmployeeGroup(EmployeeGroup employeeGroup, Boolean isCustom) {
        String sql = "INSERT INTO EmployeeGroup (name, is_custom) VALUES (?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, employeeGroup.getName());
            preparedStatement.setBoolean(2, isCustom);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Long getGroupIdByName(String groupName) {
        String sql = "SELECT group_id FROM EmployeeGroup WHERE name = ?";
        Long groupId = null;

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, groupName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    groupId = resultSet.getLong("group_id");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return groupId;
    }

    public void deleteEmployeeGroup(Long groupId) {
        String sql = "DELETE FROM EmployeeGroup WHERE group_id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, groupId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
