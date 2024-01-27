package com.example.jakoo.dao;

import com.example.jakoo.DatabaseConnector;
import com.example.jakoo.entity.Task;
import com.example.jakoo.entity.TaskTabItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    private final DatabaseConnector connector;

    public TaskDAO(DatabaseConnector connector) {
        this.connector = connector;
    }

    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM Task";
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Task task = new Task();
                task.setTaskId(resultSet.getLong("task_id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setEmployees(resultSet.getLong("employees"));
                task.setResponsible(resultSet.getLong("responsible"));
                task.setResults(resultSet.getString("results"));
                task.setStatus(resultSet.getLong("status"));
                task.setCreationDate(resultSet.getTimestamp("creation_date"));
                task.setPriority(resultSet.getLong("priority"));
                tasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return tasks;
    }

    public List<TaskTabItem> getAllTaskTabItems() {
        String sql = "SELECT task.*, priority.priority AS priority_value, status.status AS status_value, " +
                "(SELECT STRING_AGG(CONCAT('#', tag.name), ' ') " +
                " FROM tag " +
                " JOIN tasktag ON tag.tag_id = tasktag.tag_id " +
                " WHERE tasktag.task_id = task.task_id) AS task_tags " +
                " FROM task " +
                " JOIN priority ON task.priority = priority.priority_id " +
                " JOIN status ON task.status = status.status_id " +
                " ORDER BY task.title;";

        List<TaskTabItem> taskTabItems = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Task task = new Task();
                task.setTaskId(resultSet.getLong("task_id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setEmployees(resultSet.getLong("employees"));
                task.setResponsible(resultSet.getLong("responsible"));
                task.setResults(resultSet.getString("results"));
                task.setStatus(resultSet.getLong("status"));
                task.setCreationDate(resultSet.getTimestamp("creation_date"));
                task.setPriority(resultSet.getLong("priority"));

                String tags = resultSet.getString("task_tags");
                String priorityValue = resultSet.getString("priority_value");
                String statusValue = resultSet.getString("status_value");

                TaskTabItem taskTabItem = new TaskTabItem(task, tags, priorityValue, statusValue);
                taskTabItems.add(taskTabItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskTabItems;
    }

    public TaskTabItem getTaskTabItemById(long taskId) {
        String sql = "SELECT task.*, priority.priority AS priority_value, status.status AS status_value, " +
                "(SELECT STRING_AGG(CONCAT('#', tag.name), ' ') " +
                " FROM tag " +
                " JOIN tasktag ON tag.tag_id = tasktag.tag_id " +
                " WHERE tasktag.task_id = task.task_id) AS task_tags " +
                "FROM task " +
                "JOIN priority ON task.priority = priority.priority_id " +
                "JOIN status ON task.status = status.status_id " +
                "WHERE task.task_id = ?";

        TaskTabItem taskTabItem = null;

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, taskId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Task task = new Task();
                    task.setTaskId(resultSet.getLong("task_id"));
                    task.setTitle(resultSet.getString("title"));
                    task.setDescription(resultSet.getString("description"));
                    task.setEmployees(resultSet.getLong("employees"));
                    task.setResponsible(resultSet.getLong("responsible"));
                    task.setResults(resultSet.getString("results"));
                    task.setStatus(resultSet.getLong("status"));
                    task.setCreationDate(resultSet.getTimestamp("creation_date"));
                    task.setPriority(resultSet.getLong("priority"));

                    String tags = resultSet.getString("task_tags");
                    String priorityValue = resultSet.getString("priority_value");
                    String statusValue = resultSet.getString("status_value");

                    taskTabItem = new TaskTabItem(task, tags, priorityValue, statusValue);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskTabItem;
    }

    public List<TaskTabItem> searchByTitleAndTag(long pageSize, long pageNumber, String title, String tagName) {
        String sql = "SELECT DISTINCT t.*, p.priority AS priority_value, s.status AS status_value, " +
                "(SELECT STRING_AGG(CONCAT('#', tag.name), ' ') " +
                " FROM tag " +
                " JOIN tasktag ON tag.tag_id = tasktag.tag_id " +
                " WHERE tasktag.task_id = t.task_id) AS task_tags " +
                "FROM task t " +
                "JOIN priority p ON t.priority = p.priority_id " +
                "JOIN status s ON t.status = s.status_id " +
                "JOIN tasktag tt ON t.task_id = tt.task_id " +
                "JOIN tag ON tt.tag_id = tag.tag_id " +
                "WHERE LOWER(t.title) LIKE ? ";

        if (!tagName.isEmpty() && !tagName.isBlank()) {
            sql += "AND LOWER(tag.name) IN ("+ tagName +")";
        }

        sql += "LIMIT ? OFFSET ?";

        List<TaskTabItem> taskTabItems = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, "%" + title.toLowerCase() + "%");

            long offset = ((pageNumber - 1) * pageSize);

            preparedStatement.setLong(2, pageSize);
            preparedStatement.setLong(3, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Task task = new Task();
                    task.setTaskId(resultSet.getLong("task_id"));
                    task.setTitle(resultSet.getString("title"));
                    task.setDescription(resultSet.getString("description"));
                    task.setEmployees(resultSet.getLong("employees"));
                    task.setResponsible(resultSet.getLong("responsible"));
                    task.setResults(resultSet.getString("results"));
                    task.setStatus(resultSet.getLong("status"));
                    task.setCreationDate(resultSet.getTimestamp("creation_date"));
                    task.setPriority(resultSet.getLong("priority"));

                    String tags = resultSet.getString("task_tags");
                    String priorityValue = resultSet.getString("priority_value");
                    String statusValue = resultSet.getString("status_value");

                    TaskTabItem taskTabItem = new TaskTabItem(task, tags, priorityValue, statusValue);
                    taskTabItems.add(taskTabItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskTabItems;
    }

    public List<TaskTabItem> getTaskTabItemsByPage(long pageSize, long pageNumber) {
        String sql = "SELECT " +
                "    task.*," +
                "    priority.priority AS priority_value," +
                "    status.status AS status_value," +
                "    (" +
                "        SELECT STRING_AGG(CONCAT('#', tag.name), ' ')" +
                "        FROM tag" +
                "        JOIN tasktag ON tag.tag_id = tasktag.tag_id" +
                "        WHERE tasktag.task_id = task.task_id" +
                "    ) AS task_tags" +
                " FROM task" +
                " JOIN priority ON task.priority = priority.priority_id" +
                " JOIN status ON task.status = status.status_id" +
                " ORDER BY task.creation_date DESC" +
                " LIMIT ? OFFSET ?";

        List<TaskTabItem> taskTabItems = new ArrayList<>();

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            long offset = ((pageNumber - 1) * pageSize);

            preparedStatement.setLong(1, pageSize);
            preparedStatement.setLong(2, offset);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Task task = new Task();
                    task.setTaskId(resultSet.getLong("task_id"));
                    task.setTitle(resultSet.getString("title"));
                    task.setDescription(resultSet.getString("description"));
                    task.setEmployees(resultSet.getLong("employees"));
                    task.setResponsible(resultSet.getLong("responsible"));
                    task.setResults(resultSet.getString("results"));
                    task.setStatus(resultSet.getLong("status"));
                    task.setCreationDate(resultSet.getTimestamp("creation_date"));
                    task.setPriority(resultSet.getLong("priority"));

                    String tags = resultSet.getString("task_tags");
                    String priorityValue = resultSet.getString("priority_value");
                    String statusValue = resultSet.getString("status_value");

                    TaskTabItem taskTabItem = new TaskTabItem(task, tags, priorityValue, statusValue);
                    taskTabItems.add(taskTabItem);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return taskTabItems;
    }

    public Long getTaskIdByName(String taskName) {
        String sql = "SELECT task_id FROM Task WHERE title = ?";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, taskName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("task_id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Long getTaskPageNumber(long itemsPerPAge) {
        String sql = "SELECT (COUNT(*) / ?) AS PageCount FROM Task";

        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setLong(1, itemsPerPAge);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getLong("PageCount") + 1;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void addTask(Task task) {
        String sql = "INSERT INTO Task (title, description, employees, responsible, results, status, creation_date, priority) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setLong(3, task.getEmployees());
            preparedStatement.setLong(4, task.getResponsible());
            preparedStatement.setString(5, task.getResults());
            preparedStatement.setLong(6, task.getStatus());
            preparedStatement.setTimestamp(7, task.getCreationDate());
            preparedStatement.setLong(8, task.getPriority());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateTask(Task task) {
        String sql = "UPDATE Task SET title = ?, description = ?, employees = ?, responsible = ?, " +
                "results = ?, status = ?, creation_date = ?, priority = ? WHERE task_id = ?";
        try (Connection connection = connector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, task.getTitle());
            preparedStatement.setString(2, task.getDescription());
            preparedStatement.setLong(3, task.getEmployees());
            preparedStatement.setLong(4, task.getResponsible());
            preparedStatement.setString(5, task.getResults());
            preparedStatement.setLong(6, task.getStatus());
            preparedStatement.setTimestamp(7, task.getCreationDate());
            preparedStatement.setLong(8, task.getPriority());
            preparedStatement.setLong(9, task.getTaskId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTask(Task task) {
        String deleteTaskSql = "DELETE FROM Task WHERE task_id = ?";
        String deleteEmployeeGroupSql = "DELETE FROM EmployeeGroup WHERE group_id = ?";

        try (Connection connection = connector.getConnection()) {
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement deleteTaskStatement = connection.prepareStatement(deleteTaskSql);
                 PreparedStatement deleteEmployeeGroupStatement = connection.prepareStatement(deleteEmployeeGroupSql)) {

                // Delete from Task table
                deleteTaskStatement.setLong(1, task.getTaskId());
                deleteTaskStatement.executeUpdate();

                // Delete from EmployeeGroup table (twice for each task)
                // Replace taskId1 and taskId2 with the appropriate group_id values
                deleteEmployeeGroupStatement.setLong(1, task.getEmployees());
                deleteEmployeeGroupStatement.executeUpdate();

                deleteEmployeeGroupStatement.setLong(1, task.getResponsible());
                deleteEmployeeGroupStatement.executeUpdate();

                connection.commit(); // Commit the transaction
            } catch (SQLException e) {
                connection.rollback(); // Rollback the transaction in case of an exception
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
