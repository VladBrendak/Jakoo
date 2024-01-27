package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.Tag;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {
    private DatabaseConnector connector;

    public TagDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public void addTag(Tag tag) {
        String sql = "INSERT INTO Tag (name) VALUES (?)";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, tag.getName());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Tag> getAllTags() {
        String sql = "SELECT * FROM Tag";
        List<Tag> tags = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Tag tag = new Tag();
                tag.setTagID(resultSet.getLong("tag_id"));
                tag.setName(resultSet.getString("name"));
                tags.add(tag);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public List<Tag> getAllTagsWithRelative(long taskId) {
        String sql = "SELECT tag.tag_id, tag.name, " +
                "CASE " +
                "    WHEN tag.tag_id IN " +
                "        (SELECT tasktag.tag_id FROM tag JOIN tasktag ON tag.tag_id = tasktag.tag_id WHERE tasktag.task_id = ?) " +
                "    THEN true " +
                "    ELSE false " +
                "END AS relative " +
                "FROM tag";

        List<Tag> tags = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, taskId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Tag tag = new Tag();
                    tag.setTagID(resultSet.getLong("tag_id"));
                    tag.setName(resultSet.getString("name"));
                    tag.setRelateTo(resultSet.getBoolean("relative"));
                    tags.add(tag);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tags;
    }

    public void deleteTag(long tagId) {
        String sql = "DELETE FROM Tag WHERE tag_id = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, tagId);

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

