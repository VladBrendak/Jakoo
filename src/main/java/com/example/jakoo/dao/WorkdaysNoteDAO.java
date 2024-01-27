package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.WorkdaysNote;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                workdaysNote.setNoteId(resultSet.getLong("note"));
                workdaysNote.setWorkTime(resultSet.getLong("work_time"));
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
                    workdaysNote.setNoteId(resultSet.getLong("note"));
                    workdaysNote.setWorkTime(resultSet.getLong("work_time"));
                    workdaysNote.setDate(resultSet.getTimestamp("date"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return workdaysNote;
    }

    public void addWorkdaysNote(WorkdaysNote workdaysNote) {
        String sql = "INSERT INTO workdays_note (employee, workday, note, work_time, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, workdaysNote.getEmloyeeId());
            preparedStatement.setLong(2, workdaysNote.getWorkdayId());
            preparedStatement.setLong(3, workdaysNote.getNoteId());
            preparedStatement.setLong(4, workdaysNote.getWorkTime());
            preparedStatement.setTimestamp(5, workdaysNote.getDate());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateWorkdaysNote(WorkdaysNote workdaysNote) {
        String sql = "UPDATE workdays_note SET employee = ?, workday = ?, note = ?, work_time = ?, date = ? WHERE workdays_note_id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, workdaysNote.getEmloyeeId());
            preparedStatement.setLong(2, workdaysNote.getWorkdayId());
            preparedStatement.setLong(3, workdaysNote.getNoteId());
            preparedStatement.setLong(4, workdaysNote.getWorkTime());
            preparedStatement.setTimestamp(5, workdaysNote.getDate());
            preparedStatement.setLong(6, workdaysNote.getWorkdaysNoteId());

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
