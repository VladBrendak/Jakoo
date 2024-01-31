package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.WorkdaysNote;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WorkdaysNoteDAO {

    private final DatabaseConnector connector;

    public WorkdaysNoteDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<WorkdaysNote> getAllWorkdaysNotes() {
        String sql = "SELECT * FROM workdays_note";
        List<WorkdaysNote> workdaysNotesList = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                WorkdaysNote workdaysNote = new WorkdaysNote();
                workdaysNote.setWorkdaysNoteId(resultSet.getLong("workdays_note_id"));
                workdaysNote.setEmloyeeId(resultSet.getLong("employee"));
                workdaysNote.setWorkdayId(resultSet.getLong("workday"));
                workdaysNote.setNote(resultSet.getString("note"));
                workdaysNote.setPaid_additional_time(resultSet.getLong("paid_additional_time"));
                workdaysNote.setUnpaid_additional_time(resultSet.getLong("unpaid_additional_time"));
                workdaysNote.setDate(resultSet.getTimestamp("date"));
                workdaysNotesList.add(workdaysNote);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workdaysNotesList;
    }

    public WorkdaysNote getWorkdaysNoteById(Long workdaysNoteId) {
        String sql = "SELECT * FROM workdays_note WHERE workdays_note_id = ?";
        WorkdaysNote workdaysNote = null;

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, workdaysNoteId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    workdaysNote = new WorkdaysNote();
                    workdaysNote.setWorkdaysNoteId(resultSet.getLong("workdays_note_id"));
                    workdaysNote.setEmloyeeId(resultSet.getLong("employee"));
                    workdaysNote.setWorkdayId(resultSet.getLong("workday"));
                    workdaysNote.setNote(resultSet.getString("note"));
                    workdaysNote.setPaid_additional_time(resultSet.getLong("paid_additional_time"));
                    workdaysNote.setUnpaid_additional_time(resultSet.getLong("unpaid_additional_time"));
                    workdaysNote.setDate(resultSet.getTimestamp("date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workdaysNote;
    }

    public WorkdaysNote getWorkdayNoteByIdAndDate(Long employeeId, Timestamp date) {
        String sql = "SELECT * FROM workdays_note WHERE employee = ? AND date = ?";
        WorkdaysNote workdaysNote = null;

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, employeeId);
            preparedStatement.setTimestamp(2, date);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    workdaysNote = new WorkdaysNote();
                    workdaysNote.setWorkdaysNoteId(resultSet.getLong("workdays_note_id"));
                    workdaysNote.setEmloyeeId(resultSet.getLong("employee"));
                    workdaysNote.setWorkdayId(resultSet.getLong("workday"));
                    workdaysNote.setNote(resultSet.getString("note"));
                    workdaysNote.setPaid_additional_time(resultSet.getLong("paid_additional_time"));
                    workdaysNote.setUnpaid_additional_time(resultSet.getLong("unpaid_additional_time"));
                    workdaysNote.setDate(resultSet.getTimestamp("date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return workdaysNote;
    }

    public void upsertWorkdaysNote(WorkdaysNote workdaysNote) {
        String checkIfExistsSql = "SELECT workdays_note_id FROM workdays_note WHERE date = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement checkIfExistsStatement = connection.prepareStatement(checkIfExistsSql)) {
                checkIfExistsStatement.setTimestamp(1, workdaysNote.getDate());
                try (ResultSet resultSet = checkIfExistsStatement.executeQuery()) {
                    if (resultSet.next()) {
                        workdaysNote.setWorkdaysNoteId(resultSet.getLong("workdays_note_id"));
                        updateWorkdaysNote(workdaysNote);
                    } else {
                        addWorkdaysNote(workdaysNote);
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addWorkdaysNote(WorkdaysNote workdaysNote) {
        String sql = "INSERT INTO workdays_note (employee, workday, note, paid_additional_time, unpaid_additional_time, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, workdaysNote.getEmloyeeId());
            preparedStatement.setLong(2, workdaysNote.getWorkdayId());
            preparedStatement.setString(3, workdaysNote.getNote());
            preparedStatement.setLong(4, workdaysNote.getPaid_additional_time());
            preparedStatement.setLong(5, workdaysNote.getUnpaid_additional_time());
            preparedStatement.setTimestamp(6, workdaysNote.getDate());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWorkdaysNote(WorkdaysNote workdaysNote) {
        String sql = "UPDATE workdays_note SET employee = ?, workday = ?, note = ?, paid_additional_time = ?, unpaid_additional_time = ?, date = ? WHERE workdays_note_id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, workdaysNote.getEmloyeeId());
            preparedStatement.setLong(2, workdaysNote.getWorkdayId());
            preparedStatement.setString(3, workdaysNote.getNote());
            preparedStatement.setLong(4, workdaysNote.getPaid_additional_time());
            preparedStatement.setLong(5, workdaysNote.getUnpaid_additional_time());
            preparedStatement.setTimestamp(6, workdaysNote.getDate());
            preparedStatement.setLong(7, workdaysNote.getWorkdaysNoteId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteWorkdaysNote(Long workdaysNoteId) {
        String sql = "DELETE FROM workdays_note WHERE workdays_note_id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, workdaysNoteId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
