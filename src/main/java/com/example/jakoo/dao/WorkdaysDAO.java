package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.Workdays;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkdaysDAO {

    private final DatabaseConnector connector;

    public WorkdaysDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<Workdays> getAllWorkdays() {
        String sql = "SELECT * FROM Workdays";
        List<Workdays> workdaysList = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Workdays workdays = new Workdays();
                workdays.setWorkdayId(resultSet.getLong("workday_id"));
                workdays.setStatus(resultSet.getString("status"));
                workdaysList.add(workdays);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workdaysList;
    }

    public Workdays getWorkdayById(Long workdayId) {
        String sql = "SELECT * FROM Workdays WHERE workday_id = ?";
        Workdays workdays = null;

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, workdayId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    workdays = new Workdays();
                    workdays.setWorkdayId(resultSet.getLong("workday_id"));
                    workdays.setStatus(resultSet.getString("status"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workdays;
    }
}
