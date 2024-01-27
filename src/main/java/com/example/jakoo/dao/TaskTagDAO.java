package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class TaskTagDAO {
    private DatabaseConnector connector;

    public TaskTagDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public void addTaskTag(Long taskId, Long tagId) {
        String sql = "INSERT INTO TaskTag (task_id, tag_id) VALUES (?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, taskId);
            preparedStatement.setLong(2, tagId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addTaskTagsByIds(long taskId, List<Long> tagIds) {
        String sql = "INSERT INTO TaskTag (task_id, tag_id) VALUES (?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Long tagId : tagIds) {
                preparedStatement.setLong(1, taskId);
                preparedStatement.setLong(2, tagId);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTaskTagsByIds(long taskId, List<Long> tagIds) {
        if (tagIds.isEmpty()) {
            return; // Nothing to delete
        }

        String sql = "DELETE FROM TaskTag WHERE task_id = ? AND tag_id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            for (Long tagId : tagIds) {
                preparedStatement.setLong(1, taskId);
                preparedStatement.setLong(2, tagId);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

