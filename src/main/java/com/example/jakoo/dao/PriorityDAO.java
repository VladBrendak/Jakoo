package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.Priority;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PriorityDAO {

    private final DatabaseConnector connector;

    public PriorityDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<Priority> getAllPriorities() {
        String sql = "SELECT * FROM Priority";
        List<Priority> priorityList = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Priority priority = new Priority();
                priority.setPriorityId(resultSet.getLong("priority_id"));
                priority.setPriority(resultSet.getString("priority"));
                priorityList.add(priority);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return priorityList;
    }

    public Priority getPriorityById(Long priorityId) {
        String sql = "SELECT * FROM Priority WHERE priority_id = ?";
        Priority priority = null;

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, priorityId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    priority = new Priority();
                    priority.setPriorityId(resultSet.getLong("priority_id"));
                    priority.setPriority(resultSet.getString("priority"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return priority;
    }
}
