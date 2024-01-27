package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.Status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StatusDAO {

    private final DatabaseConnector connector;

    public StatusDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<Status> getAllStatuses() {
        String sql = "SELECT * FROM Status";
        List<Status> statusList = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Status status = new Status();
                status.setStatusId(resultSet.getLong("status_id"));
                status.setStatus(resultSet.getString("status"));
                statusList.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return statusList;
    }

    public Status getStatusById(Long statusId) {
        String sql = "SELECT * FROM Status WHERE status_id = ?";
        Status status = null;

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, statusId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    status = new Status();
                    status.setStatusId(resultSet.getLong("status_id"));
                    status.setStatus(resultSet.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return status;
    }
}
