package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.Note;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NoteDAO {

    private final DatabaseConnector connector;

    public NoteDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<Note> getAllNotes() {
        String sql = "SELECT * FROM Note";
        List<Note> notes = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Note note = new Note();
                note.setNoteId(resultSet.getLong("note_id"));
                note.setEmloyeeId(resultSet.getLong("employee"));
                note.setText(resultSet.getString("text"));
                note.setDate(resultSet.getTimestamp("date"));
                notes.add(note);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return notes;
    }

    public void addNote(Note note) {
        String sql = "INSERT INTO Note (employee, text, date) VALUES (?, ?, ?)";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, note.getEmloyeeId());
            preparedStatement.setString(2, note.getText());
            preparedStatement.setTimestamp(3, note.getDate());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateNote(Note note) {
        String sql = "UPDATE Note SET employee = ?, text = ?, date = ? WHERE note_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, note.getEmloyeeId());
            preparedStatement.setString(2, note.getText());
            preparedStatement.setTimestamp(3, note.getDate());
            preparedStatement.setLong(4, note.getNoteId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteNote(Long noteId) {
        String sql = "DELETE FROM Note WHERE note_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, noteId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

